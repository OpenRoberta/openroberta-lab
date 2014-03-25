package lejos.hardware.device;

import lejos.hardware.port.I2CException;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;
import lejos.utility.Delay;
import lejos.utility.EndianTools;

/**
 * Supports the Mindsensors NXTMMX motor multiplexor. This device allows you to connect two 
 * additional motors to your robot using a sensor port. Multiple NXTMMXs can be chained together when addressed correctly.
 * <p>
 * Create an instance of this class and use the factory methods to provide a <code>MMXRegulatedMotor</code> or 
 * <code>MMXMotor</code> instance.
 * @see MMXRegulatedMotor
 * @see MMXMotor
 * @author Kirk P Thompsonn
 * 
 */
public class NXTMMX extends I2CSensor {
	/** 
	 * Represents Motor 1 as indicated on the controller
     */
    public static final int MOTOR_M1 = 0;
    /** 
     * Represents Motor 2 as indicated on the controller
     */
    public static final int MOTOR_M2 = 1;
    
    /**
     * The default I2C address (0x06) for the NXTMMX.
     */
    public static final int DEFAULT_MMX_ADDRESS = 0x6;
    
    private static final int CHANNELS = 2;
    
    static final int CMD_FORWARD = 0;
    static final int CMD_BACKWARD = 1;
    static final int CMD_FLT = 2;
    static final int CMD_STOP = 3;
    static final int CMD_SETPOWER = 4;
    static final int CMD_ROTATE = 5;
    static final int CMD_ROTATE_TO = 6;
    static final int CMD_GETPOWER = 7;
    static final int CMD_RESETTACHO = 8;
    static final int CMD_SETRAMPING = 9;
    static final int CMD_ISSTALLED = 10;
    static final int CMD_ISMOVING = 11;
    static final int CMD_SETREGULATE = 12;
    static final int CMD_GETTACHO = 13;
    static final int CMD_GETSPEED = 14;
    static final int CMD_GETLIMITANGLE = 15;
    
    // motor state management
    int[] motorState = {STATE_STOPPED, STATE_STOPPED};
    private static final int STATE_STOPPED = 0;
    private static final int STATE_RUNNING_FWD = 1;
    private static final int STATE_RUNNING_BKWD = 2;
    private static final int STATE_ROTATE_TO = 3;
    
    // register map for the motor-specific registers
    private static final int REG_IDX_ENCODER_TARGET = 0;
    private static final int REG_IDX_COMMAND = 1;
    private static final int REG_IDX_POWER = 2;
//    private static final int REG_IDX_TIME = 3;
    private static final int REG_IDX_STATUS = 4;
    private static final int REG_IDX_ENCODER_CURRENT = 5;
    static final int[][] REGISTER_MAP = // [REG_IDX_xxx][channel/motor ID]
        {{0x42, 0x4A}, // Encoder Target (write)	: s/int
         {0x49, 0x51}, // Command A					: byte
         {0x46, 0x4E}, // Power (aka speed)			: s/byte
         {0x47, 0x4F}, // Time						: byte
         {0x72, 0x73}, // Status					: byte 
         {0x62, 0x66}  // Encoder Value (read)		: s/int
        };
    
    // common registers
    static final int REG_ENCODERSREAD = REGISTER_MAP[REG_IDX_ENCODER_CURRENT][MOTOR_M1]; // used by tachmonitor to read both encoders at once
    private static final int REG_MMXCOMMAND = 0x41;
    
    // MMX common commands
    private static final byte MMXCOMMAND_RESET = 0x52;
    private static final byte MMXCOMMAND_BRAKE = 0x43;
    private static final byte MMXCOMMAND_FLOAT = 0x63;
    
    // MMX channel commands 
    private static final byte MMXCOMMAND_IDX_BRAKE = 0;
    private static final byte MMXCOMMAND_IDX_FLOAT = 1;
    private static final byte MMXCOMMAND_IDX_ENCODER_RST = 2;
    static final byte[][] MMXCOMMAND_MAP = // [MMXCOMMAND_xxx][channel/motor ID]
        {{0x41, 0x42}, 	// Brake
         {0x61, 0x62}, 	// Float
         {0x72, 0x73} 	// tacho reset
        };
    
    // Command register OR masks
    private static final int CMDBIT_SPEED_MODE = 0x01;
    private static final int CMDBIT_RAMP = 0x02;
    private static final int CMDBIT_ROTATE_RELATIVE = 0x04;
    private static final int CMDBIT_ROTATE_MODE = 0x08;
    private static final int CMDBIT_BRAKING_MODE = 0x10;
    private static final int CMDBIT_HOLD_POSITION = 0x20;
//    private static final int CMDBIT_TIMED_MODE = 0x40;
    private static final int CMDBIT_GO = 0x80;
    
    // motor parameters
    private int[][] motorParams = new int[5][CHANNELS]; 
    private static final int MOTPARAM_POWER = 0; // current power value
    private static final int MOTPARAM_RAMPING = 1; // 0=false=none, 1=ramped
    private static final int MOTPARAM_ENCODER_BRAKING = 2; // 1=brake, 0= float
    private static final int MOTPARAM_ENCODER_HOLD = 3; // 1=lock to position after rotation completion, 0=don't
    private static final int MOTPARAM_REGULATE = 4; // 1=set CMDBIT_SPEED_MODE on command issue, 0=don't
    static final int MOTPARAM_OP_TRUE=1;
    static final int MOTPARAM_OP_FALSE=0;
    
    // motor and monitor instance buckets
    Object[] motors= new Object[CHANNELS];
    BUSYMonitor[] bUSYMonitors = new BUSYMonitor[CHANNELS];
    TachoMonitor tachoMonitor;
    boolean[] busyMonitorWaiting = {false,false};
    
    private static final byte MOTTYPE_EMPTY = -1;
    private static final byte MOTTYPE_BASIC = 0;
    private static final byte MOTTYPE_REGULATED = 1;
    byte[] motorType = {MOTTYPE_EMPTY,MOTTYPE_EMPTY};
    
    // I2C buffer
    private byte[] buf = new byte[12];
    
    private int limitangle[] = {0,0};
	
    // When a rotate is issued, an instance monitors the status bits and sets STATE_STOPPED when the command completes.
    // Also does any required RegulatedMotorListener STOP and notifies any wait() in waitRotateComplete().
    // Used by rotate() method only.
    private class BUSYMonitor extends Thread {
        int channel;
        BUSYMonitor(int channel){
            this.channel = channel;
        }
        @Override
		public void run(){
        	byte buf1[] = {1};
        	while ((buf1[0] & 0x0F) != 0) {
                Delay.msDelay(50);
                //System.out.println("bsy " + buf1[0] + " ");
                getData(REGISTER_MAP[REG_IDX_STATUS][channel], buf1, 1);
            }
            if (motorState[channel]!=STATE_STOPPED) {
            	motorState[channel]=STATE_STOPPED;
	            if (motorType[channel]==MOTTYPE_REGULATED) {
	            	((MMXRegulatedMotor) motors[channel]).doListenerState(MMXRegulatedMotor.LISTENERSTATE_STOP);
	            }
            }
            //System.out.println("busy done");
            // exit any wait for state of [ImmediateReturn = false]
            synchronized (bUSYMonitors[channel]) {
            	busyMonitorWaiting[channel]=false;
            	bUSYMonitors[channel].notifyAll();
//            	System.out.println("*notifyall");
        	}
        }
    }
    
    // used by MMXRegulatedMotor to monitor/manage tacho-related
    private class TachoMonitor extends Thread{
        private static final int POLL_DELAY_MS = 75;
        
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
                retVal = 0;
                try {
                	getData(REG_ENCODERSREAD, buffer, 8);
                } catch (I2CException e) {
                	retVal = -1;
                }
 
                failCount = 0;
                while (retVal < 0) {
                    if (++failCount>4) {
//                    	System.out.println("fail");
                    	continue Main;
                    }
                    retVal = 0;
                    try {
                    	getData(REG_ENCODERSREAD, buffer, 8);
                    } catch (I2CException e) {
                    	retVal = -1;
                    }
                }
                
                // baseline the time after successful I2C transaction
                endTime = System.currentTimeMillis();
                timeDelta = endTime - beginTime;
                beginTime = endTime;
                
                // parse the buffer into the counts
                synchronized (this){
                    TachoCount[MOTOR_M1] = EndianTools.decodeIntLE(buffer, 0);
                    TachoCount[MOTOR_M2] = EndianTools.decodeIntLE(buffer, 4);
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

    /**
     * Constructor for the NXTMMX
     * @param port - the port its plugged in to
     */
    public NXTMMX(I2CPort port) {
        this(port, DEFAULT_MMX_ADDRESS);
    }

    /**
     * Constructor for the NXTMMX
     * @param port - the port its plugged in to
     */
    public NXTMMX(Port port) {
        this(port, DEFAULT_MMX_ADDRESS);
    }

    private void init()
    {
        // resets mux values to default and stops all tasks. This includes zeroing the tachos.
        sendData(REG_MMXCOMMAND, MMXCOMMAND_RESET);
        Delay.msDelay(50);
        // init motor operation parameters
        for (int i=0;i<CHANNELS;i++){
            motorParams[MOTPARAM_RAMPING][i] = MOTPARAM_OP_TRUE;
            motorParams[MOTPARAM_ENCODER_BRAKING][i] = MOTPARAM_OP_TRUE;
            motorParams[MOTPARAM_POWER][i] = 0;
            motorParams[MOTPARAM_REGULATE][i] = MOTPARAM_OP_TRUE;
            doCommand(CMD_SETPOWER, 100, i); // will set motorParams[MOTPARAM_POWER][channel]
        }
        
    }
    /**
     * Constructor for the NXTMMX
     * @param port - the sensor port its plugged in to
     * @param address The I2C address for the device
     */
    public NXTMMX(I2CPort port, int address) {
        super(port, address);
        init();
    }

    /**
     * Constructor for the NXTMMX
     * @param port - the sensor port its plugged in to
     * @param address The I2C address for the device
     */
    public NXTMMX(Port port, int address) {
        super(port, address, TYPE_LOWSPEED);
        init();
    }
	
    /**
     * Get a <code>MMXRegulatedMotor</code> instance that is associated with the <code>motorID</code>. 
     * 
     * @param motorID The motor ID number. This is indicated on the NXTMMX Motor Controller and is
     * represented using <code> {@link #MOTOR_M1}</code> or <code> {@link #MOTOR_M2}</code>.
     * @return The <code>MMXRegulatedMotor</code> instance 
     * @throws IllegalArgumentException if invalid <code>motorID</code>
     * @throws UnsupportedOperationException if <code>motorID</code> has already been used for a 
     * <code>MMXMotor</code> motor instance.
     * @see MMXRegulatedMotor
     * @see #getBasicMotor
     */
	public MMXRegulatedMotor getRegulatedMotor(int motorID) {
        getMotor(motorID, MOTTYPE_REGULATED);
        return (MMXRegulatedMotor) motors[motorID];
    }
	
    /**
     * Get a <code>MMXMotor</code> instance that is associated with the <code>motorID</code>. 
     * 
     * @param motorID The motor ID number. This is indicated on the NXTMMX Motor Controller and is
     * represented using <code> {@link #MOTOR_M1}</code> or <code> {@link #MOTOR_M2}</code>.
     * @return The <code>MMXMotor</code> instance 
     * @throws IllegalArgumentException if invalid <code>motorID</code>
     * @throws UnsupportedOperationException if <code>motorID</code> has already been used for a 
     * <code>MMXRegulatedMotor</code> motor instance.
     * @see MMXMotor
     * @see #getRegulatedMotor
     */
	public MMXMotor getBasicMotor(int motorID) {
        getMotor(motorID, MOTTYPE_BASIC);
        return (MMXMotor) motors[motorID];
    }
	
	private void getMotor(int motorID, byte motorTypeValue){
    	if (motorID<MOTOR_M1 || motorID>MOTOR_M2) {
            throw new IllegalArgumentException("Invalid motor ID");
        }
    
        if (motorType[motorID]==MOTTYPE_EMPTY) {
        	switch(motorTypeValue) {
	        	case MOTTYPE_REGULATED:
	        		motors[motorID]=new MMXRegulatedMotor(this, motorID);
	        		//start the tacho monitor if not already for encoder-enabled motor
	        		if (tachoMonitor==null) {
	                    this.tachoMonitor = new TachoMonitor();
	                    this.tachoMonitor.start();
	                }
	        		break;
	        	case MOTTYPE_BASIC:
	        		motors[motorID]=new MMXMotor(this, motorID);
        	}
        	motorType[motorID]=motorTypeValue;
        }
        if (motorType[motorID]!=motorTypeValue) {
        	throw new UnsupportedOperationException("Wrong motor type");
        }
    }
	 
	boolean tachoMonitorAlive() {
        return tachoMonitor!=null && tachoMonitor.isAlive();
    }
	
	/** 
     * Execute a command. designed to never block because it is shared across two motors. The rotate WAITS are done
     * in the MMXRegulatedMotor class
     * @param command the command
     * @param operand the value from the caller. Mostly not used and set to 0
     * @param channel the channel: MOTOR_M1, MOTOR_M2
     * @return a value depending on the command
     */
    synchronized int doCommand(int command, int operand, int channel) {
        byte workingByte=0;
        int commandRetVal=0;
        
        switch (command) {
            case CMD_FORWARD:
            case CMD_BACKWARD:
                if (command==CMD_FORWARD && motorState[channel]==STATE_RUNNING_FWD) break;
                if (command==CMD_BACKWARD && motorState[channel]==STATE_RUNNING_BKWD) break;
                if (motorType[channel]==MOTTYPE_REGULATED) {
                	if (motorState[channel]!=STATE_STOPPED) {
                		((MMXRegulatedMotor) motors[channel]).doListenerState(MMXRegulatedMotor.LISTENERSTATE_STOP);
                	}
                	((MMXRegulatedMotor) motors[channel]).doListenerState(MMXRegulatedMotor.LISTENERSTATE_START);
                }
                // command + 1 = STATE_RUNNING_BKWD or STATE_RUNNING_FWD
        		setDirection(channel, command + 1);
        		commandMotor(channel, 0);
                break;
            case CMD_FLT:
            	workingByte = MMXCOMMAND_MAP[MMXCOMMAND_IDX_FLOAT][channel];
            case CMD_STOP:
                if (command==CMD_STOP) workingByte = MMXCOMMAND_MAP[MMXCOMMAND_IDX_BRAKE][channel];
            	sendData(REG_MMXCOMMAND, workingByte);
                if (motorType[channel]==MOTTYPE_REGULATED && motorState[channel]!=STATE_STOPPED ) {
                	((MMXRegulatedMotor) motors[channel]).doListenerState(MMXRegulatedMotor.LISTENERSTATE_STOP);
                }
                
                motorState[channel]=STATE_STOPPED;
                break;
            case CMD_SETPOWER: // requires positive value and validation done in caller
            	// exit if no change
            	if (motorParams[MOTPARAM_POWER][channel]==operand) break;
            	
            	// set the (abs) power value in global param array
            	motorParams[MOTPARAM_POWER][channel] = operand;
                
                // set the power with proper sign based on direction
                workingByte = (byte)operand;
                if (motorState[channel]==STATE_RUNNING_BKWD ) {
                    workingByte *= -1;
                }
                sendData(REGISTER_MAP[REG_IDX_POWER][channel], workingByte); 
                
                // if not running or in a rotate, exit
                if (motorState[channel]==STATE_STOPPED || motorState[channel]==STATE_ROTATE_TO) break;
                
                // set the power if running (but not in a rotate) to take effect immediately
                commandMotor(channel, 0);
                break;
            case CMD_ROTATE:
            case CMD_ROTATE_TO:
            	if (motorType[channel]==MOTTYPE_REGULATED) {
                	if (motorState[channel]!=STATE_STOPPED) {
                    	// ensure the motor has stopped before new rotate so listener event timing is correct
//                		sendData(REG_MMXCOMMAND, MMXCOMMAND_MAP[MMXCOMMAND_IDX_BRAKE][channel]);
//                    	while (tachoMonitor.isMoving(channel)) Delay.msDelay(50);
                		((MMXRegulatedMotor) motors[channel]).doListenerState(MMXRegulatedMotor.LISTENERSTATE_STOP);
                	}
                	((MMXRegulatedMotor) motors[channel]).doListenerState(MMXRegulatedMotor.LISTENERSTATE_START);
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
            	sendData(REG_MMXCOMMAND, MMXCOMMAND_MAP[MMXCOMMAND_IDX_ENCODER_RST][channel]);
            	Delay.msDelay(50);
                break;
            case CMD_ISMOVING:
                commandRetVal=MOTPARAM_OP_TRUE;
                if (motorState[channel]==STATE_STOPPED) commandRetVal=MOTPARAM_OP_FALSE;
                // over-ride previous decision if regulated motor and it is moving
                if (tachoMonitorAlive()) {
                	commandRetVal = tachoMonitor.isMoving(channel)?MOTPARAM_OP_TRUE:MOTPARAM_OP_FALSE;
                }
                break;
            case CMD_ISSTALLED:
            	commandRetVal=MOTPARAM_OP_FALSE;
            	if (tachoMonitorAlive() && motorState[channel]!=STATE_STOPPED && !tachoMonitor.isMoving(channel)) {
            		commandRetVal=MOTPARAM_OP_TRUE;
            	}
            	break;
            case CMD_SETREGULATE:
                motorParams[MOTPARAM_REGULATE][channel]=operand; //1=true, 0=false                
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
            case CMD_SETRAMPING:
            	motorParams[MOTPARAM_RAMPING][channel]=operand; //1=true, 0=false    
                break;
                
            default:
                throw new IllegalArgumentException("Invalid Command");
        }
        return commandRetVal;   
    }
    
    private void setDirection(int channel, int directionState) {
    	if (motorState[channel]!=directionState){
    		// set the power with proper sign based on desired direction state
    		motorState[channel]=directionState;
    		byte workingByte=(byte) motorParams[MOTPARAM_POWER][channel];
    		if (motorState[channel]==STATE_RUNNING_BKWD ) {
                workingByte *= -1;
            }
    		sendData(REGISTER_MAP[REG_IDX_POWER][channel], workingByte);
    	}
    }
    
	private int getEncoderValue(int channel) {
		// TODO the old mmx getTacho requeried i2c on error return. Do we need this?
		
		// use latest tachoMonitor value for the motor if available
		if (tachoMonitorAlive()) {
			return tachoMonitor.getTachoCount(channel);
		}
		// .. otherwise, query the controller to get it
		getData(REGISTER_MAP[REG_IDX_ENCODER_CURRENT][channel], buf, 4);
		return EndianTools.decodeIntLE(buf, 0);
	}
  
	private void rotate(int channel, int value, int cmd) {
		byte workingByte = CMDBIT_ROTATE_MODE;
		
		// set cmd bits
		if (cmd == CMD_ROTATE) {
			workingByte |= CMDBIT_ROTATE_RELATIVE; //2
		} 
		// TODO these two need to be settable somewhere
		if (motorParams[MOTPARAM_ENCODER_BRAKING][channel] == MOTPARAM_OP_TRUE) {
			workingByte |= CMDBIT_BRAKING_MODE; //4
		}
		if (motorParams[MOTPARAM_ENCODER_HOLD][channel] == MOTPARAM_OP_TRUE) {
			workingByte |= CMDBIT_HOLD_POSITION; //5
		}
		
		// Wait until previous busyMonitor is done TODO
//		if (bUSYMonitors[channel]!=null){
//			try {
//				System.out.println("prejoin");
//				if (bUSYMonitors[channel].isAlive()) bUSYMonitors[channel].join();
//				System.out.println("postjoin");
//			} catch (InterruptedException e) {
//				// ignore
//			}
//		}
//		while(busyMonitorWaiting[channel]) Delay.msDelay(50);
		
        // set the target angle
        this.limitangle[channel] = value; // for getLimitAngle()
        EndianTools.encodeIntLE(value, buf, 0);
		sendData(REGISTER_MAP[REG_IDX_ENCODER_TARGET][channel], buf, 4);
		motorState[channel] = STATE_ROTATE_TO;
		
		// set up the RegulatedMotorListener notifier and waitToComplete
		// notifier for the rotate
		bUSYMonitors[channel] = new BUSYMonitor(channel);
		// do the rotate
		commandMotor(channel, workingByte);
		bUSYMonitors[channel].start();
	}
  
	void waitRotateComplete(int channel) {
		synchronized (bUSYMonitors[channel]) {
			busyMonitorWaiting[channel] = true;
			while (busyMonitorWaiting[channel]) {
				try {
					bUSYMonitors[channel].wait();
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}
	
	/**
	 * sets motor command mask to the NXTMMX motor command register with appropriate bit masks set 
	 * on passed command value.
	 * @param channel motor ID
	 * @param maskValue value to be ORed (CMDBITs)
	 */
	private void commandMotor(int channel, int maskValue){
		byte workingByte = (byte)((CMDBIT_GO | maskValue) & 0xff);

		if (motorParams[MOTPARAM_REGULATE][channel] == MOTPARAM_OP_TRUE) workingByte |= CMDBIT_SPEED_MODE;
		if (motorParams[MOTPARAM_RAMPING][channel] == MOTPARAM_OP_TRUE) workingByte |= CMDBIT_RAMP;
		// send the message.
		sendData(REGISTER_MAP[REG_IDX_COMMAND][channel], workingByte);
	}
	
	/**
	 * Returns the voltage supplied to the NXTMMX
	 * @return the voltage in volts
	 */
	public synchronized float getVoltage(){
		 getData(REG_MMXCOMMAND, buf, 1);
		 // 37 is the constant given by Mindsensors support 5/2011 to return millivolts. KPT
         return (37f*(buf[0]&0xff))*.001f;
	}
	
	private void synchStop(byte command){
		sendData(REG_MMXCOMMAND, command);
		for (int channel=0;channel<CHANNELS;channel++){
			if (motorType[channel]==MOTTYPE_REGULATED && motorState[channel]!=STATE_STOPPED ) {
	        	((MMXRegulatedMotor) motors[channel]).doListenerState(MMXRegulatedMotor.LISTENERSTATE_STOP);
	        }
			motorState[channel]=STATE_STOPPED;
		}
	}
	
	/**
	 * Synchronized stop both motors
	 */
	public void stopMotors(){
		synchStop(MMXCOMMAND_BRAKE);
	}
	
	/**
	 * Synchronized Float both motors
	 */
	public void fltMotors(){
		synchStop(MMXCOMMAND_FLOAT);
	}
}
