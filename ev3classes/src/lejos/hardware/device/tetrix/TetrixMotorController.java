package lejos.hardware.device.tetrix;

import lejos.hardware.port.I2CPort;
import lejos.hardware.sensor.I2CSensor;
import lejos.utility.Delay;
import lejos.utility.EndianTools;


/**
 * HiTechnic Tetrix Motor Controller abstraction. Provides <code>TetrixMotor</code> and <code>TetrixEncoderMotor</code> instances 
 * which are used to control the Tetrix motors.
 * <p>
 * Use <code>{@link TetrixControllerFactory#newMotorController}</code> to retrieve a <code>TetrixMotorController</code> instance.
 * 
 * @see lejos.nxt.addon.tetrix.TetrixControllerFactory
 * @see lejos.nxt.addon.tetrix.TetrixMotor
 * @see lejos.nxt.addon.tetrix.TetrixEncoderMotor
 * @author Kirk P. Thompson
 */
public class TetrixMotorController extends I2CSensor {
    /** Represents Motor 1 as indicated on the controller
     */
    public static final int MOTOR_1 = 0;
    /** Represents Motor 2 as indicated on the controller
     */
    public static final int MOTOR_2 = 1;
    
    private static final int CHANNELS = 2;
    private static final int KEEPALIVE_PING_INTERVAL = 2450;
    
    static final int CMD_FORWARD = 0;
    static final int CMD_BACKWARD = 1;
    static final int CMD_FLT = 2;
    static final int CMD_STOP = 3;
    static final int CMD_SETPOWER = 4;
    static final int CMD_ROTATE = 5;
    static final int CMD_ROTATE_TO = 6;
    static final int CMD_GETPOWER = 7;
    static final int CMD_RESETTACHO = 8;
    static final int CMD_SETREVERSE = 10;
    static final int CMD_ISMOVING = 11;
    static final int CMD_SETREGULATE = 12;
    static final int CMD_GETTACHO = 13;
    static final int CMD_GETSPEED = 14;
    static final int CMD_GETLIMITANGLE = 15;
    
    int[] motorState = {STATE_STOPPED, STATE_STOPPED};
    private static final int STATE_STOPPED = 0;
    private static final int STATE_RUNNING_FWD = 1;
    private static final int STATE_RUNNING_BKWD = 2;
    private static final int STATE_ROTATE_TO = 3;
    
    // common registers
    private static final int REG_ALL_MOTORCONTROL = 0x40;
    private static final int REG_BATTERY = 0x54;
    private static final int REG_ENCODERSREAD = 0x4C; // used by tachmonitor to read both encoders at once
    // register map for the motor-specific registers
    private static final int REG_IDX_ENCODER_TARGET = 0;
    private static final int REG_IDX_MODE = 1;
    private static final int REG_IDX_POWER = 2;
    private static final int REG_IDX_ENCODER_CURRENT = 3;
    static final int[][] REGISTER_MAP = // [REG_IDX_xxx][channel]
        {{0x40, 0x48}, // Encoder Target (write)
         {0x44, 0x47}, // Mode
         {0x45, 0x46}, // Power
         {0x4C, 0x50}  // Encoder Value (read)
        };
    
    // Mode OR masks
    private static final int MODEBIT_REVERSE = 0x08;
//    private static final int MODEBIT_NTO = 0x10;
//    private static final int MODEBIT_ERROR = 0x40;
    private static final int MODEBIT_BUSY = 0x80;
    private static final int MODEBIT_SEL_POWER = 0x00;
    private static final int MODEBIT_SEL_SPEED = 0x01;
    private static final int MODEBIT_SEL_POSITION = 0x02;
    private static final int MODEBIT_SEL_RST_ENCODER = 0x03;
    
    // motor parameters
    private int[][] motorParams = new int[4][CHANNELS]; 
    private static final int MOTPARAM_POWER = 0; // current power value
    private static final int MOTPARAM_REGULATED = 1; // 0=false=power control, 1=speed control
    private static final int MOTPARAM_REVERSED = 2; // 1=reversed, 0= normal
    private static final int MOTPARAM_ROTATE = 3; // 1=rotate to target mode
    static final int MOTPARAM_OP_TRUE=1;
    static final int MOTPARAM_OP_FALSE=0;
    
    // motor and monitor instance buckets
    Object[] motors= new Object[CHANNELS];
    BUSYMonitor[] bUSYMonitors = new BUSYMonitor[CHANNELS];
    TachoMonitor tachoMonitor;
    boolean[] busyMonitorWaiting = {false,false};
    
    private static final byte MOTTYPE_EMPTY = -1;
    private static final byte MOTTYPE_BASIC = 0;
    private static final byte MOTTYPE_ENCODER = 1;
    private static final byte MOTTYPE_REGULATED = 2;
    byte[] motorType = {MOTTYPE_EMPTY,MOTTYPE_EMPTY};
    
    // I2C buffer
    private byte[] buf = new byte[12];
    
    private int[] limitangle = {0,0};

    /**
     * Instantiate a HiTechnic TETRIX Motor Controller connected to the given <code>port</code> and daisy chain position.
     * 
     * @param port The sensor port the controller (if daisy-chained, the first) is connected to.
     * @param daisyChainPosition The position of the controller in the daisy chain.
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_1
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_2
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_3
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_4
     * @see lejos.nxt.SensorPort
     * @throws IllegalStateException if a Motor Controller was not found with given <code>port</code> and <code>daisyChainPosition</code>
     */
    public TetrixMotorController(I2CPort port, int daisyChainPosition) {
        super(port, daisyChainPosition);
        address = daisyChainPosition;
        if (!(getVendorID().equalsIgnoreCase(TetrixControllerFactory.TETRIX_VENDOR_ID) && 
            getProductID().equalsIgnoreCase(TetrixControllerFactory.TETRIX_MOTORCON_PRODUCT_ID))) {
            throw new IllegalStateException("Not a motor controller");
        }
        initController();
        
        // This thread will keep the controller active. Without I2C activity within 2.5 seconds, it times out.
        // We could use the NTO bit of mode (MODEBIT_NTO) to keep the controller from timing
        // out but the motors would still run if the NXT faulted, was shutdown, etc. which could be unsafe with big,
        // metal robots with sharp slicing attachments. 
        Thread t1 = new Thread(new Runnable(){
            public void run() {
                byte[] buf1 = new byte[1];
                for (;;){
                    getData(REG_VERSION, buf1, 0);
                    Delay.msDelay(KEEPALIVE_PING_INTERVAL);
                    // let the thread die if we are constantly getting tachocounts as this will keep the contoller active instead
                    if (tachoMonitorAlive()) break;
                }
            }
        });
        t1.setDaemon(true);
        t1.start();
    }
    
    // When a rotate is issued, an instance monitors the BUSY bit and sets STATE_STOPPED when the command completes.
    // Also does any required RegulatedMotorListener STOP and notifies any wait() in waitRotateComplete().
    // Used by rotate() method only.
    private class BUSYMonitor extends Thread {
        int channel;
        BUSYMonitor(int channel){
            this.channel = channel;
        }
        @Override
		public void run(){
            byte buf1[] = {(byte)MODEBIT_BUSY};
            while ((buf1[0] & MODEBIT_BUSY) == MODEBIT_BUSY) {
                Delay.msDelay(100);
                //System.out.println("bsy");
                getData(REGISTER_MAP[REG_IDX_MODE][channel], buf1, 1);
            }
            motorState[channel]=STATE_STOPPED;
            if (motorType[channel]==MOTTYPE_REGULATED) {
            	((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_STOP);
            }
            //System.out.println("busy done");
            // exit any wait for state of [ImmediateReturn = false]
            synchronized (bUSYMonitors[channel]) {
            	busyMonitorWaiting[channel]=false;
            	bUSYMonitors[channel].notify();
        	}
        }
    }
    
    // used by TetrixRegulatedMotor to monitor/manage tacho-related
    private class TachoMonitor extends Thread{
        private static final int POLL_DELAY_MS = 100;
        
        private boolean threadDie = false;
        private int[] TachoCount = new int[CHANNELS];
        private byte[] buffer = new byte[8];
        private volatile float[] degpersec = new float[CHANNELS];
        private float[][] samples = new float[CHANNELS][3];
        private int sampleIndex=0;
        private boolean[] ismoving = new boolean[CHANNELS];
        
        TachoMonitor(){
            this.setDaemon(true);
        }
        
        synchronized int getTachoCount(int channel) {
            return TachoCount[channel];
        }
        
        synchronized int getSpeed(int channel) {
            return (int)(degpersec[channel] * 100);
        }
        
        boolean isMoving(int channel){
        	return ismoving[channel];	
        }
        
        @Override
		public void run(){
            int retVal;
            int failCount;
            int[] tachoBegin = new int[CHANNELS];
            long endTime, beginTime, timeDelta;
            float[] degpersecAccum = new float[CHANNELS];
            
            beginTime = System.currentTimeMillis();
            Main: while (!threadDie){
                Delay.msDelay(POLL_DELAY_MS);
                getData(REG_ENCODERSREAD, buffer, 8);
                
                // baseline the time after successful I2C transaction
                endTime = System.currentTimeMillis();
                timeDelta = endTime - beginTime;
                beginTime = endTime;
                
                // parse the buffer into the counts
                synchronized (this){
                    TachoCount[MOTOR_1] = EndianTools.decodeIntBE(buffer, 0);
                    TachoCount[MOTOR_2] = EndianTools.decodeIntBE(buffer, 4);
                }
                
                // Do velocity calcs (deg per sec) 
                for (int i=0;i<CHANNELS;i++) {
                    synchronized (this){
                        // rotate the index if needed
                        if (i==CHANNELS-1) {
                            if (++sampleIndex >= samples[i].length) sampleIndex = 0;
                        }
                        // save a dps sample
                        samples[i][sampleIndex] = (float)Math.abs(TachoCount[i] - tachoBegin[i]) / timeDelta * 250;
                        tachoBegin[i] = TachoCount[i];
                    }
                    
                    // average the samples and the last result (degpersec)
                    for (int ii=0;ii<samples[i].length;ii++) {
                        degpersecAccum[i] += samples[i][ii];
                    }
                    this.degpersec[i] = degpersecAccum[i] / (samples[i].length + 1);
                    degpersecAccum[i] = this.degpersec[i];
                    synchronized (this){
                    	ismoving[i] = (((int)degpersec[i])!=0);
                    }
                }
            }
        }
    }
    
    boolean tachoMonitorAlive() {
        return tachoMonitor!=null && tachoMonitor.isAlive();
    }
    
    /**
     * Get a <code>TetrixMotor</code> instance that is associated with the <code>motorID</code>.
     * 
     * @param motorID The motor ID number. This is indicated on the HiTechnic Motor Controller and is
     * represented using <code> {@link #MOTOR_1}</code> or <code> {@link #MOTOR_2}</code>.
     * @return The <code>TetrixMotor</code> instance 
     * @throws IllegalArgumentException if invalid <code>motorID</code>
     * @throws UnsupportedOperationException if <code>motorID</code> has already been used for a Tetrix motor instance
     * other than <code>TetrixMotor</code>.
     * @see lejos.nxt.addon.tetrix.TetrixMotor
     * @see #getEncoderMotor
     * @see #getRegulatedMotor
     */
	public TetrixMotor getBasicMotor(int motorID) {
        getMotor(motorID, MOTTYPE_BASIC);
        return (TetrixMotor) motors[motorID];
    }
    
    /**
     * Get a <code>TetrixEncoderMotor</code> instance that is associated with the <code>motorID</code>. The motor must
     * have an encoder installed for a <code>TetrixEncoderMotor</code> instance to work correctly.
     * 
     * @param motorID The motor ID number. This is indicated on the HiTechnic Motor Controller and is
     * represented using <code> {@link #MOTOR_1}</code> or <code> {@link #MOTOR_2}</code>.
     * @return The <code>TetrixEncoderMotor</code> instance 
     * @throws IllegalArgumentException if invalid <code>motorID</code>
     * @throws UnsupportedOperationException if <code>motorID</code> has already been used for a Tetrix motor instance
     * other than <code>TetrixEncoderMotor</code>.
     * @see lejos.nxt.addon.tetrix.TetrixEncoderMotor
     * @see #getBasicMotor
     * @see #getRegulatedMotor
     */
    public TetrixEncoderMotor getEncoderMotor(int motorID) {
        getMotor(motorID, MOTTYPE_ENCODER);
        return (TetrixEncoderMotor) motors[motorID];
    }
    
    /**
     * Get a <code>TetrixRegulatedMotor</code> instance that is associated with the <code>motorID</code>. The motor must
     * have an encoder installed for a <code>TetrixRegulatedMotor</code> instance to work correctly.
     * 
     * @param motorID The motor ID number. This is indicated on the HiTechnic Motor Controller and is
     * represented using <code> {@link #MOTOR_1}</code> or <code> {@link #MOTOR_2}</code>.
     * @return The <code>TetrixRegulatedMotor</code> instance 
     * @throws IllegalArgumentException if invalid <code>motorID</code>
     * @throws UnsupportedOperationException if <code>motorID</code> has already been used for a Tetrix motor instance
     * other than <code>TetrixRegulatedMotor</code>.
     * @see TetrixRegulatedMotor
     * @see #getBasicMotor
     * @see #getEncoderMotor
     */
    public TetrixRegulatedMotor getRegulatedMotor(int motorID) {
        getMotor(motorID, MOTTYPE_REGULATED);
        return (TetrixRegulatedMotor) motors[motorID];
    }
    
    private void getMotor(int motorID, byte motorTypeValue){
    	if (motorID<MOTOR_1 || motorID>MOTOR_2) {
            throw new IllegalArgumentException("Invalid motor ID");
        }
    
        if (motorType[motorID]==MOTTYPE_EMPTY) {
        	switch(motorTypeValue) {
	        	case MOTTYPE_REGULATED:
	        		motors[motorID]=new TetrixRegulatedMotor(this, motorID);
	        		break;
	        	case MOTTYPE_ENCODER:
	        		motors[motorID]=new TetrixEncoderMotor(this, motorID);
	        		break;
	        	case MOTTYPE_BASIC:
	        		motors[motorID]=new TetrixMotor(this, motorID);
        	}
        	motorType[motorID]=motorTypeValue;
        }
        if (motorType[motorID]!=motorTypeValue) {
        	throw new UnsupportedOperationException("Wrong motor type");
        }

        //start the tacho monitor if not already for encoder-enabled motors
        if (motorTypeValue > MOTTYPE_BASIC && tachoMonitor==null) {
            this.tachoMonitor = new TachoMonitor();
            this.tachoMonitor.start();
        }
    }
    
//    void dumpArray(int[] arr) {
//        for (int i=0;i<arr.length;i++) {
//            System.out.print(arr[i] + ":");
//        }
//        System.out.println("");
//        Button.waitForAnyPress();
//    }
  
    private void setMode(int channel, boolean resetEncoder) {
//        int mode=MODEBIT_SEL_POWER | MODEBIT_NTO;
        int mode=MODEBIT_SEL_POWER; 
        
        // constant speed SEL bit and not in ROTATE (POS) state, set the bit. This is done because
        // if we set both the MODEBIT_SEL_SPEED and MODEBIT_SEL_POSITION, it equals MODEBIT_SEL_RST_ENCODER and
        // cancels out the ROTATE command
        if (motorParams[MOTPARAM_REGULATED][channel]==MOTPARAM_OP_TRUE && motorState[channel]!=STATE_ROTATE_TO) {
            mode = mode | MODEBIT_SEL_SPEED;
        } 
        // run to position SEL bit
        if (motorParams[MOTPARAM_ROTATE][channel]==MOTPARAM_OP_TRUE) {
            mode = mode | MODEBIT_SEL_POSITION;
        }
        // reverse operation bit
        if (motorParams[MOTPARAM_REVERSED][channel]==MOTPARAM_OP_TRUE) {
            mode = mode | MODEBIT_REVERSE;
        }
        // if encoder reset requested
        if (resetEncoder) {
            mode = mode |  MODEBIT_SEL_RST_ENCODER;
        }
        
        // set the mode
        sendData(REGISTER_MAP[REG_IDX_MODE][channel], (byte)(mode & 0xff));
    }
    
    private int getEncoderValue(int channel) {
        // use latest tachoMonitor value for the motor if available
        if (tachoMonitorAlive()) {
            return tachoMonitor.getTachoCount(channel);
        }
        // .. otherwise, query the controller to get it
        getData(REGISTER_MAP[REG_IDX_ENCODER_CURRENT][channel], buf, 4);
        return EndianTools.decodeIntBE(buf, 0);
    }
    
    private void rotate(int channel, int value, int cmd){
        byte workingByte=0;
        motorParams[MOTPARAM_ROTATE][channel]=MOTPARAM_OP_TRUE;
        
        if (cmd==CMD_ROTATE) {
            // set the target based current + degrees passed
            value = getEncoderValue(channel) + value * 4;
        } else if(cmd==CMD_ROTATE_TO) {
            value *= 4;
        } else return;
        
        this.limitangle[channel] = Math.round(value * .25f) ;
        
        // set the encoder position
        EndianTools.encodeIntBE(value, buf, 0);
        sendData(REGISTER_MAP[REG_IDX_ENCODER_TARGET][channel], buf, 4); 
        motorState[channel]=STATE_ROTATE_TO;
        
        // set the mode
        setMode(channel, false);
        
        // set the power to turn on the motor. Ensure it is positive (do not adjust for BACKWARDS)
        workingByte=(byte)motorParams[MOTPARAM_POWER][channel];
        sendData(REGISTER_MAP[REG_IDX_POWER][channel], workingByte); 
        
        // set up the RegulatedMotorListener notifier and waitToComplete notifier for the rotate
        bUSYMonitors[channel] = new BUSYMonitor(channel);
        bUSYMonitors[channel].start();
        
        return;
    }
    
    void waitRotateComplete(int channel) {
    	synchronized (bUSYMonitors[channel]) {
    		busyMonitorWaiting[channel]=true;
    		while (busyMonitorWaiting[channel]) {
	    		try {
					bUSYMonitors[channel].wait();
				} catch (InterruptedException e) {
					// ignore
				}
    		}
    	}
    }
    
    private void motorGo(int channel, int command) {
        byte workingByte=0;
        int retval;
        
        motorState[channel]=command + 1; // STATE_RUNNING_FWD, STATE_RUNNING_BKWD assuming command IN(CMD_FORWARD,CMD_BACKWARD)
        motorParams[MOTPARAM_ROTATE][channel]=MOTPARAM_OP_FALSE; //false
        // set the mode
        setMode(channel, false);
        // set the power to turn on the motor
        workingByte=(byte)motorParams[MOTPARAM_POWER][channel];
        if (command==CMD_BACKWARD) {
            workingByte*=-1; // negative power runs backwards
        }
        sendData(REGISTER_MAP[REG_IDX_POWER][channel], workingByte); 
    }

    /** 
     * Execute a command. designed to never block because it is shared across two motors. The rotate WAITS are done
     * in the TetrixEncoderMotor class
     * @param command the command
     * @param operand the value from the caller. Mostly not used and set to 0
     * @param channel the channel: MOTOR_1, MOTOR_2
     * @return a value depending on the command
     */
    synchronized int doCommand(int command, int operand, int channel) {
        byte workingByte=0;
        int commandRetVal=0;
        
        switch (command) {
            case CMD_FORWARD:
                if (motorState[channel]==STATE_RUNNING_FWD) break;
                if (motorType[channel]==MOTTYPE_REGULATED) {
                	if (motorState[channel]!=STATE_STOPPED) {
                		((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_STOP);
                	}
                	((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_START);
                }
                motorGo(channel, command);
                break;
            case CMD_BACKWARD:
                if (motorState[channel]==STATE_RUNNING_BKWD) break;
                if (motorType[channel]==MOTTYPE_REGULATED) {
                	if (motorState[channel]!=STATE_STOPPED) {
                		((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_STOP);
                	}
                	((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_START);
                }
                motorGo(channel, command);
                break;
            case CMD_FLT:
                workingByte=-128;
            case CMD_STOP:
                if (command==CMD_STOP) workingByte=0;
                sendData(REGISTER_MAP[REG_IDX_POWER][channel], workingByte); 
                Delay.msDelay(50);
                if (motorType[channel]==MOTTYPE_REGULATED && motorState[channel]!=STATE_STOPPED && motorState[channel]!=STATE_ROTATE_TO) {
                	((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_STOP);
                }
                motorState[channel]=STATE_STOPPED;
                break;
            case CMD_SETPOWER:
                motorParams[MOTPARAM_POWER][channel] = operand;
                // if not running, exit
                if (motorState[channel]==STATE_STOPPED)  break;
                
                // set the power if running to take effect immediately
                workingByte = (byte)motorParams[MOTPARAM_POWER][channel];
                if (motorState[channel]==STATE_RUNNING_BKWD ) {
                    workingByte *= -1;
                }
                sendData(REGISTER_MAP[REG_IDX_POWER][channel], workingByte); 
                break;
            case CMD_ROTATE:
            case CMD_ROTATE_TO:
            	if (motorType[channel]==MOTTYPE_REGULATED) {
                	if (motorState[channel]!=STATE_STOPPED) {
                		((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_STOP);
                	}
                	((TetrixRegulatedMotor) motors[channel]).doListenerState(TetrixRegulatedMotor.LISTENERSTATE_START);
                }
                rotate(channel, operand, command);
                break;
            case CMD_GETPOWER:
                commandRetVal=motorParams[MOTPARAM_POWER][channel];
                break;
            case CMD_GETTACHO:
                commandRetVal=getEncoderValue(channel);
                break;
            case CMD_RESETTACHO:
                // reset encoder/tacho 
                setMode(channel, true);
                Delay.msDelay(30); // small delay to allow encoder value reset in controller to happen
                motorState[channel]=STATE_STOPPED;
                break;
            case CMD_SETREVERSE:
                motorParams[MOTPARAM_REVERSED][channel]=operand;
                setMode(channel, false);
                break;
            case CMD_ISMOVING:
                commandRetVal=MOTPARAM_OP_TRUE;
                if (motorState[channel]==STATE_STOPPED) commandRetVal=MOTPARAM_OP_FALSE;
                // over-ride previous decision if regulated motor and it is moving
                if (tachoMonitorAlive()) {
                	commandRetVal = tachoMonitor.isMoving(channel)?MOTPARAM_OP_TRUE:MOTPARAM_OP_FALSE;
                }
                break;
            case CMD_SETREGULATE:
                motorParams[MOTPARAM_REGULATED][channel]=operand; //1=true, 0=false                
                break;
            case CMD_GETSPEED:
                commandRetVal = 0;
                if (tachoMonitorAlive()) {
                    commandRetVal = tachoMonitor.getSpeed(channel);
                }
                break;
            case CMD_GETLIMITANGLE:
                commandRetVal = limitangle[channel];
                break;
            default:
                throw new IllegalArgumentException("Invalid Command");
        }
        return commandRetVal;   
        
    }
    
    private void initController() {
        byte[] initBuf = {0,0,0,0,0,0,0,0,0,0,0,0};
        sendData(REG_ALL_MOTORCONTROL, initBuf, initBuf.length);
        Delay.msDelay(50);
        // reset motor params, encoder/tacho, and set NTO mode
//        byte mode = (byte)(MODEBIT_SEL_RST_ENCODER | MODEBIT_NTO) & 0xff;
        byte mode = (byte)MODEBIT_SEL_RST_ENCODER & 0xff;
        for (int i = 0;i<CHANNELS;i++){
            motorParams[MOTPARAM_POWER][i] = 0;         // current power value
            motorParams[MOTPARAM_REGULATED][i] = MOTPARAM_OP_FALSE;     // 0=false=power control, 1=speed control
            motorParams[MOTPARAM_REVERSED][i] = MOTPARAM_OP_FALSE;      // 1=reversed, 0= normal
            motorParams[MOTPARAM_ROTATE][i] = MOTPARAM_OP_FALSE;        // 1=rotate to target mode, 0=no rotate
            sendData(REGISTER_MAP[REG_IDX_MODE][i], mode);
        }
    }
    
    /** 
     * Return the current battery voltage supplied to the controller.
     * @return The current battery voltage in volts
     */
    public synchronized float getVoltage() {
        int retVal;
        
        getData(REG_BATTERY, buf, 2);
        retVal=(buf[0] & 0xff)<<2;
        retVal=retVal | (buf[1] & 0x03);
        return retVal * .02f;
    }
}

