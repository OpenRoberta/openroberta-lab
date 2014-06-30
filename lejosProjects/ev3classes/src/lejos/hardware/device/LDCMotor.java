package lejos.hardware.device;

import lejos.hardware.port.I2CPort;

/**
 * LDCMotor, Lattebox DC Motor, is a abstraction to model any DCMotor connected to
 * LSC, Lattebox Servo Controller. 
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class LDCMotor extends LMotor{

	private int speed;
	private int forward_min_speed = 1020;
	private int forward_max_speed = 850;
	private int backward_min_speed = 1080;
	private int backward_max_speed = 1230;

	/**
	 * Constructor
	 * 
	 * @param port
	 * @param location
	 * @param DCMotorName
	 * @param SPI_PORT
	 *  
	 */
	public LDCMotor(I2CPort port, int location, String DCMotorName, byte SPI_PORT){
		super(port,location,DCMotorName,SPI_PORT);
	}

	public LDCMotor(I2CPort port, int location, String DCMotorName, byte SPI_PORT,int forwardMinSpeed,int forwardMaxSpeed,int backwardMinSpeed, int backwardMaxSpeed){
		super(port,location,DCMotorName,SPI_PORT);
		
		this.forward_min_speed = forwardMinSpeed;
		this.forward_max_speed = forwardMaxSpeed;
		this.backward_min_speed = backwardMinSpeed;
		this.backward_max_speed = backwardMaxSpeed;
	}
	
	/**
	 * Method to set the speed in a DC Motor 
	 * 
	 * @param speed the speed
	 * 
	 */
	public void setSpeed(int speed){
		this.setPulse(speed);
	}
	
	/**
	 * 
	 * Method to get speed from the DC Motor
	 *
	 * @return the speed
	 * 
	 */
	public int getSpeed(){
		return this.getPulse();
	}
	
	public void setForwardMinSpeed(int min_speed){
		this.forward_min_speed = min_speed;
	}

	public void setForwardMaxSpeed(int max_speed){
		this.forward_max_speed = max_speed;
	}

	public void setBackwardMinSpeed(int min_speed){
		this.backward_min_speed = min_speed;
	}

	public void setBackwardMaxSpeed(int max_speed){
		this.backward_max_speed = max_speed;
	}
}
