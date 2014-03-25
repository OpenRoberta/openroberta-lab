package lejos.hardware.device.tetrix;

import lejos.hardware.port.I2CPort;
import lejos.hardware.sensor.I2CSensor;
import lejos.utility.Delay;

// This class was developed using the HiTec HS-475HB medium duty servo as the use/test case:
// Control System: +Pulse Width Control 1500usec Neutral
// Required Pulse: 3-5 Volt Peak to Peak Square Wave
// Operating Voltage: 4.8-6.0 Volts
// Operating Temperature Range: -20 to +60 Degree C
// Operating Speed (4.8V): 0.22sec/60 deg at no load
// Operating Speed (6.0V): 0.18sec/60 deg at no load
// Stall Torque (4.8V): 66.6 oz/in. (4.8kg.cm)
// Stall Torque (6.0V): 83.3 oz/in. (6.0kg.cm)
// Operating Angle: 45 Deg. one side pulse traveling 400usec
// 360 Modifiable: Yes
// Direction: Clockwise/Pulse Traveling 1500 to 1900usec
// Current Drain (4.8V): 8mA/idle and 150mA no load operating
// Current Drain (6.0V): 8.8mA/idle and 180mA no load operating
// Dead Band Width: 8usec
// Motor Type: 3 Pole Ferrite Motor
// Potentiometer Drive: Indirect Drive
// Bearing Type: Top Ball Bearing, Lower Bushing
// Gear Type: Karbonite Gears
// Connector Wire Length: 11.81" (300mm)
// Dimensions: See Schematic
// Weight: 1.59oz (45g)

/**
 * HiTechnic Servo Controller abstraction. Provides <code>TetrixServo</code> instances which are used to control
 * the Tetrix servos.
 * <p>
 * Servos are driven by a PWM signal with varying pulse widths
 * controlling the rotational position of the servo actuator.
 * The pulse nominally ranges from 1.0 ms to 2.0 ms with 1.5 ms always being center of range. 
 * Pulse widths outside this range can be used for "overtravel" -moving the servo beyond its normal range.
 * <p>
 * As an example, for a servo with
 * a 90 deg. travel range, a pulse width of 1.5 ms (1500 microseconds) will typically set the servo to its "neutral" position or 45 degrees, 
 * a pulse of 1.25 ms could set it to 0 degrees and a pulse of 1.75 ms to 90 degrees. 
 * The physical limits and timings of the servo hardware varies between brands and models, but a 
 * general 90 degree servo's angular motion will travel somewhere in the range of 90 deg. - 120 deg. and the 
 * neutral position is almost always at 1.5 ms. This is the "standard pulse servo mode" used by all hobby analog servos.
 * <p>
 * The HiTechic Servo Controller allows setting of the PWM output from 0.75 - 2.25ms. Note that some servos may hit their 
 * internal mechanical limits at each end of this range causing them to consume excessive current and <b>potentially be damaged</b>.
 *  <p>
 * Use <code>{@link TetrixControllerFactory#newServoController}</code> to retrieve a <code>TetrixServoController</code> instance.
 * 
 * @see lejos.nxt.addon.tetrix.TetrixControllerFactory
 * @author Kirk P. Thompson
 */
public class TetrixServoController extends I2CSensor {
    /** 
     * Represents the servo connected to Channel 1 as indicated on the controller
     */
    public static final int SERVO_1 = 0;

    /** 
     * Represents the servo connected to Channel 2 as indicated on the controller
     */
    public static final int SERVO_2 = 1;

    /** 
     * Represents the servo connected to Channel 3 as indicated on the controller
     */
    public static final int SERVO_3 = 2;

    /** 
     * Represents the servo connected to Channel 4 as indicated on the controller
     */
    public static final int SERVO_4 = 3;

    /** 
     * Represents the servo connected to Channel 5 as indicated on the controller
     */
    public static final int SERVO_5 = 4;

    /** 
     * Represents the servo connected to Channel 6 as indicated on the controller
     */
    public static final int SERVO_6 = 5;
    
    private static final int CHANNELS = 6;
    private static final int KEEPALIVE_PING_INTERVAL = 9900;
    private static final int REG_PWM_ENABLE = 0x48;
    private static final int REG_SERVO_1_POS = 0x42;
    private static final int REG_STEP_TIME = 0x41;
    private static final int REG_STATUS = 0x40;
    
    // PWM Mode values
    private static final byte PWMMODE_ENABLE = 0x00;
    private static final byte PWMMODE_DISABLE = (byte)0xFF;
//    private static final byte PWMMODE_ENABLE_NTO = (byte)0xAA;
    
    // controller characteristics
    private static final int CONTROLLER_PULSE_RANGE_LOW = 750;
    private static final int CONTROLLER_PULSE_RANGE_HIGH = 2250;
    private static final int DEFAULT_HITEC_MOTION_RANGE = 200;
    private static final float PULSE_RESOLUTION = 255f/(CONTROLLER_PULSE_RANGE_HIGH-CONTROLLER_PULSE_RANGE_LOW) ;
    
    // servo parameters
    private int[][] servoParams = new int[4][CHANNELS]; 
    private static final int SRVOPARAM_PULSEWIDTH_RANGE_LOW = 0; // low pulse width range in usec
    private static final int SRVOPARAM_PULSEWIDTH_RANGE_HIGH = 1; // high pulse width range in usec
    private static final int SRVOPARAM_ROTATION_RANGE = 2; // Range of servo in degrees
    private static final int SRVOPARAM_PULSEBYTE = 3; //  current pulse width in usec
    
    // servo instances
    private TetrixServo[] servos= new TetrixServo[CHANNELS];
    
    /**
     * Instantiate for a HiTechnic TETRIX Servo Controller connected to the given <code>port</code> and daisy chain position.
     * 
     * @param port The sensor port the controller (if daisy-chained, the first) is connected to.
     * @param daisyChainPosition The position of the controller in the daisy chain.
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_1
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_2
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_3
     * @see TetrixControllerFactory#DAISY_CHAIN_POSITION_4
     * @see lejos.nxt.SensorPort
     * @throws IllegalStateException if a Servo Controller was not found with given <code>port</code> and <code>daisyChainPosition</code>
     */
    public TetrixServoController(I2CPort port, int daisyChainPosition) {
        super(port, daisyChainPosition);
        address = daisyChainPosition;
        if (!(getVendorID().equalsIgnoreCase(TetrixControllerFactory.TETRIX_VENDOR_ID) && 
            getProductID().equalsIgnoreCase(TetrixControllerFactory.TETRIX_SERVOCON_PRODUCT_ID))) {
            throw new IllegalStateException("Not a servo controller");
        }
        initController();
        // This thread will keep the controller active. Without I2C activity within 10 seconds, it times out.
        // We could use the PWM Enable value 0xAA in REG_PWM_ENABLE to keep the controller from timing
        // out but the servos would still be powered if the NXT faulted, was shutdown, etc. 
        Thread t1 = new Thread(new Runnable(){
            public void run() {
                byte[] buf = new byte[1];
                for (;;){
                    getData(REG_VERSION, buf, 0);
                    Delay.msDelay(KEEPALIVE_PING_INTERVAL);
                }
            }
        });
        t1.setDaemon(true);
        t1.start();
    }

    /**
     * Get the <code>TetrixServo</code> instance that is associated with the passed <code>servoID</code>.
     * 
     * @param servoID The motor ID number <code>SERVO_1</code> to <code>SERVO_6</code>. This is indicated on the 
     * HiTechnic Servo Controller.
     * @return The <code>TetrixServo</code> instance associated with the labeled channel
     * @see #SERVO_1
     * @see #SERVO_2
     * @see #SERVO_3
     * @see #SERVO_4
     * @see #SERVO_5
     * @see #SERVO_6
     */
    public TetrixServo getServo(int servoID) {
        if (servoID<SERVO_1 || servoID>SERVO_6) {
            throw new IllegalArgumentException("Invalid servo ID");
        }
        if (servos[servoID]==null) servos[servoID]=new TetrixServo(this, servoID);
        return servos[servoID];
    }
    
    private void setPulse(int channel, byte pulseByte) {
        // set the pulse width
        servoParams[SRVOPARAM_PULSEBYTE][channel] = pulseByte & 0xff;
        sendData(REG_SERVO_1_POS + channel, pulseByte); 
        sendData(REG_PWM_ENABLE, PWMMODE_ENABLE); 
    }
    
    /**
     * Set the operating range of the servo in microseconds. Default at instantiation is 750 & 2250.
     * @param microsecLOW the low end of the servos response range in microseconds
     * @param microsecHIGH the high end of the servos response range in microseconds
     * @throws IllegalArgumentException if the range isn't within 750 and 2250
     */
    synchronized void setPulseRange (int channel, int microsecLOW, int microsecHIGH, int travelRange) throws IllegalArgumentException {
        if (microsecLOW < CONTROLLER_PULSE_RANGE_LOW || 
            microsecHIGH > CONTROLLER_PULSE_RANGE_HIGH || 
            microsecLOW >= microsecHIGH) {
                throw new IllegalArgumentException("Invalid range");
        }
        servoParams[SRVOPARAM_PULSEWIDTH_RANGE_LOW][channel] = microsecLOW;
        servoParams[SRVOPARAM_PULSEWIDTH_RANGE_HIGH][channel] = microsecHIGH;
        servoParams[SRVOPARAM_ROTATION_RANGE][channel] = travelRange;
    }
    
    synchronized void setPulseWidth (int channel, int pulseWidth) throws IllegalArgumentException {
        if (pulseWidth < CONTROLLER_PULSE_RANGE_LOW || pulseWidth > CONTROLLER_PULSE_RANGE_HIGH) {
            throw new IllegalArgumentException("Invalid pulse value");
        }
        byte workingByte;
        // calc byte value for pulse width
        workingByte = (byte)(Math.round((pulseWidth-CONTROLLER_PULSE_RANGE_LOW)*PULSE_RESOLUTION)&0xff);
        setPulse(channel, workingByte);
    }
    
    synchronized int getPulseWidth (int channel) {
        return (int)((float)servoParams[SRVOPARAM_PULSEBYTE][channel] / PULSE_RESOLUTION + CONTROLLER_PULSE_RANGE_LOW);
    }
    
    synchronized float getAngle(int channel) {
        float servoResolution = (servoParams[SRVOPARAM_PULSEWIDTH_RANGE_HIGH][channel] 
            - (float)servoParams[SRVOPARAM_PULSEWIDTH_RANGE_LOW][channel]) / servoParams[SRVOPARAM_ROTATION_RANGE][channel];
        float angle = (servoParams[SRVOPARAM_PULSEBYTE][channel] - (float)servoParams[SRVOPARAM_PULSEWIDTH_RANGE_LOW][channel] 
            * PULSE_RESOLUTION 
            + (float)CONTROLLER_PULSE_RANGE_LOW * PULSE_RESOLUTION) / (servoResolution * PULSE_RESOLUTION);
        return angle;
    }
    
    synchronized void setAngle(int channel, float angle){
        if (angle < 0 || angle > servoParams[SRVOPARAM_ROTATION_RANGE][channel]) {
            throw new IllegalArgumentException("Invalid range value");
        }
        // ** calc the byte value of angle using the defined ranges of the servo.
        // get the pulse resolution of the specific servo
        // multiply by requested angle
        // add offset between servo low range and contoller low range to determine actual target pulse width
        // multiply by controller range pulse resolution [inverse] constant to get byte
        float servoResolution = (servoParams[SRVOPARAM_PULSEWIDTH_RANGE_HIGH][channel] 
            - (float)servoParams[SRVOPARAM_PULSEWIDTH_RANGE_LOW][channel]) / servoParams[SRVOPARAM_ROTATION_RANGE][channel];
        int theByte = (int)(
        (   servoResolution * angle 
            + (servoParams[SRVOPARAM_PULSEWIDTH_RANGE_LOW][channel] - CONTROLLER_PULSE_RANGE_LOW)
        ) 
        * PULSE_RESOLUTION);
        setPulse(channel, (byte)(theByte & 0xff));
    }
    
    private void initController() {
        for (int i = 0;i<CHANNELS;i++){
            servoParams[SRVOPARAM_PULSEWIDTH_RANGE_LOW][i] = CONTROLLER_PULSE_RANGE_LOW;       
            servoParams[SRVOPARAM_PULSEWIDTH_RANGE_HIGH][i] = CONTROLLER_PULSE_RANGE_HIGH;   
            servoParams[SRVOPARAM_ROTATION_RANGE][i] = DEFAULT_HITEC_MOTION_RANGE;
            servoParams[SRVOPARAM_PULSEBYTE][i] = 128; // ~1500 microsecs is neutral position
            sendData(REG_SERVO_1_POS + i, (byte)(servoParams[SRVOPARAM_PULSEBYTE][i] & 0xff));
        }
    }
    
    /**
     * Returns whether or not there are servos on this controller that are moving. This method always returns 
     * <code>false</code> when the step time is set to 0 (disabled).
     * 
     * @return <code>true</code> if any servo is moving to position.
     * @see #setStepTime
     */
    public synchronized boolean isMoving(){
        byte[] buf = new byte[1];
        this.getData(REG_STATUS, buf, 1);
        return buf[0]!=0;
    }

    /** 
     * Set all servos connected to this controller to float mode. This means they are powered down and will not attempt 
     * to hold their current
     * position. The next call to any motion command for any servo will re-enable power to all servo channels.
     */
    public synchronized void flt() {
        sendData(REG_PWM_ENABLE, PWMMODE_DISABLE); 
    }
    
    /**
     * Sets the step time used for all servos on this controller. This sets the step time for the servo channel 
     * which has the furthest to move. Other servo channels which are not at their designated positions yet will run at a 
     * slower rate to ensure they reach their destination positions at the same time. The controller defaults to 0 on power-up.
     * <p>
     * The step time can be considered a delay before progressing to the next step. For example, if a servo is positioned 
     * at 1500 microseconds pulse width, and you give it a 
     * new position command of 2000 microseconds, it will normally go as fast as it can to get to the new position. If you want it to 
     * go to the new position but not at the maximum output, you can set the step to a value from 0 to 15.
     * <p>
     * One of the main things it could be useful for, is if you have two servos with different loads, and you want them 
     * to be as much in sync as possible. You can set the speed to slow the controller from changing the servo signals instantly.
     * <p>
     * The <code>isMoving()</code> method always returns <code>false</code> if the step time is set to zero.
     * 
     * @param step Step Time, 0-15. Setting to 0 disables step time.
     * @throws IllegalArgumentException If step is not in the range 0 to 15
     * @see #getStepTime
     * @see #isMoving
     */
     // TODO this needs to be tested when I get more than one servo
    public synchronized void setStepTime(int step){
        if (step < 0 || step > 15) throw new IllegalArgumentException("Invalid value");
        sendData(REG_STEP_TIME, (byte)step); 
    }
    
    /**
     * Gets the step time used for all servos on this controller.
     * @return The step time 0-15. 0 means step is disabled.
     * @see #setStepTime
     */
    public synchronized int getStepTime(){
        byte[] buf = new byte[1];
        this.getData(REG_STEP_TIME, buf, 1);
        return buf[0] & 0xff;
    }
}
