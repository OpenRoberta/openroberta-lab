package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

/**
 * Supports Mindsensors PF Mate<br>
 * This device is used to control Lego Power Function IR receiver<br>
 * 
 * @author Michael Smith <mdsmitty@gmail.com>
 * 
 */
public class PFMate extends I2CSensor{
	public PFMateMotor A;
	public PFMateMotor B;

    public static final int DEFAULT_PFMATE_ADDRESS = 0x48;
	
	private final static int CHANNEL_REG = 0x42; //register to control the channel the receiver is using
	private final static int MOTORS_REG = 0x43;  // register for motor control 
	private final static int OPER_A_REG = 0x44;  // register for motor direction of motor A
	private final static int SPEED_A_REG = 0x45;  // register for speed of motor A
	private final static int OPER_B_REG = 0x46;  // register for motor direction of motor B
	private final static int SPEED_B_REG = 0x47;  // register for speed of motor B
	
	private byte [] buffer = new byte[1];

	private void init(int channel)
	{
        setChannel(channel);
        setMotor(0);

        A = new PFMateMotor(this, OPER_A_REG, SPEED_A_REG);
        B = new PFMateMotor(this, OPER_B_REG, SPEED_B_REG);
	    
	}
    /**
     * Constructor takes in the sensor port and the PF channel you will be using
     * @param port sensor port
     * @param channel PF Channel 1-4
     */
    public PFMate(I2CPort port, int channel){
        this(port, channel, DEFAULT_PFMATE_ADDRESS);
    }

    /**
     * Constructor takes in the sensor port and the PF channel you will be using
     * @param port sensor port
     * @param channel PF Channel 1-4
     * @param address I2C address of the controller
     */
    public PFMate(I2CPort port, int channel, int address){
        super(port, address);
        init(channel);
    }

    /**
     * Constructor takes in the sensor port and the PF channel you will be using
     * @param port sensor port
     * @param channel PF Channel 1-4
     */
    public PFMate(Port port, int channel){
        this(port, channel, DEFAULT_PFMATE_ADDRESS);
    }

    /**
     * Constructor takes in the sensor port and the PF channel you will be using
     * @param port sensor port
     * @param channel PF Channel 1-4
     * @param address I2C address of the controller
     */
    public PFMate(Port port, int channel, int address){
        super(port, address, TYPE_LOWSPEED);
        init(channel);
    }

	/**
	 * Sends command to PF IR receiver to apply changes made to the registers. Call this after
	 * You have set speed, direction and/or channel.
	 */
	public void update(){
		this.sendData(0x41, (byte)0x47);
	}
	
	/**
	 * Sets PF channel to use.
	 * @param channel 1-4
	 */
	public void setChannel(int channel){
		if(channel < 1) channel = 1;
		if (channel > 4) channel = 4;
		sendData(CHANNEL_REG, (byte) channel);
	}
	
	/**
	 * Determines which motors are to be used buy default both are activated
	 * @param motor 0 both, 1 motor A or 2 motor B
	 */
	public void setMotor(int motor){
		if(motor < 0) motor = 0;
		if (motor > 2) motor = 2;
		sendData(MOTORS_REG, (byte) motor);
	}

	/**
	 * Returns the current IR channel in use by the PF Mate
	 * @return int 1-4
	 */	
	public int getChannel(){
		this.getData(CHANNEL_REG, buffer, 1);
		return buffer[0];
	}
	
	/**
	 * Returns which motors are activated
	 * @return int 0 both, 1 motor A or 2 motor B
	 */
	public int getMotor(){
		this.getData(MOTORS_REG, buffer, 1);
		return buffer[0];
	}
}


