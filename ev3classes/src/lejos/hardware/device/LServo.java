package lejos.hardware.device;

import lejos.hardware.port.I2CPort;

/**
 * LServo, Lattebox Servo, is a abstraction to model any RC Servo (continous and non continous)  plugged to
 * LSC, Lattebox Servo Controller. 
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class LServo extends LMotor{
	private int min_angle = 0;
	private int max_angle = 2000;
	private int init_angle = 1000;
	
	/**
	 * Constructor
	 * 
	 * @param port
	 * @param location
	 * @param servoName
	 * @param SPI_PORT
	 *  
	 */
	public LServo(I2CPort port, int location, String servoName, byte SPI_PORT){
		super(port,location,servoName,SPI_PORT);
	}

	/**
	 *
	 * Constructor with the feature to set min and max angle
	 *
	 * @param port
	 * @param location
	 * @param servoName
	 * @param SPI_PORT
	 * @param min_angle
	 * @param max_angle
	 *
	 */
	public LServo(I2CPort port, int location, String servoName, byte SPI_PORT,int min_angle, int max_angle){
		super(port,location,servoName,SPI_PORT);
		
		this.min_angle = min_angle;
		this.max_angle = max_angle;
	}

	/**
	 *
	 * Constructor with the feature to set min, max and init angle
	 *
	 * @param port
	 * @param location
	 * @param servoName
	 * @param SPI_PORT
	 * @param min_angle
	 * @param max_angle
	 * @param init_angle
	 *
	 */
	public LServo(I2CPort port, int location, String servoName, byte SPI_PORT,int min_angle, int max_angle,int init_angle){
		super(port,location,servoName,SPI_PORT);
		
		this.min_angle = min_angle;
		this.max_angle = max_angle;
		this.init_angle = init_angle;
	}
	
	/**
	 * Method to set an Angle in a RC Servo. 
	 * 
	 * @param angle
	 * 
	 */
	public void setAngle(int angle){
		this.setPulse(angle);
	}
	
	/**
	 * 
	 * Method to know the angle
	 *
	 * @return the angle
	 *
	 */
	public int getAngle(){
		return this.getPulse();
	}

	/**
	 * Set Minimal angle. Useful method to calibrate a Servo
	 * 
	 * @param minAngle the minimum angle
	 * 
	 */
	public void setMinAngle(int minAngle){
		this.min_angle = minAngle;
	}

	/**
	 * Set Maximum angle. Useful method to calibrate a Servo
	 * 
	 * @param maxAngle
	 * 
	 */	
	public void setMaxAngle(int maxAngle){
		this.max_angle = maxAngle;
	}	
	
	/**
	 * Method to set minimal angle
	 *  
	 */	
	public void goToMinAngle(){
		this.setAngle(this.min_angle);
	}

	/**
	 * Method to set maximum angle
	 * 
	 */	
	public void goToMaxAngle(){
		this.setAngle(this.max_angle);		
	}

	/**
	 * Method to set medium angle
	 * 
	 */		
	public void goToMiddleAngle(){
		float middle = (this.min_angle + this.max_angle) / 2;
		
		this.setAngle(Math.round(middle));
	}

	/**
	 * Method to set medium angle
	 * 
	 */		
	public void goToInitAngle(){
		this.setAngle(this.init_angle);
	}
	
	/**
	 * Classic forward method for continous RC Servos
	 * 
	 */
	public void forward(){
		this.setAngle(0);
	}

	/**
	 * Classic backward method for continous RC Servos
	 * 
	 */
	public void backward(){
		this.setAngle(2000);
	}	
}
