package lejos.hardware.device;

import lejos.hardware.port.I2CPort;
import lejos.hardware.sensor.I2CSensor;

/**
 * MServo, is a abstraction to model any RC Servo (continuous and non continuous)  plugged to
 * 
 * @author Juan Antonio Brenha Moral
 * 
 */
public class MServo extends I2CSensor{
	//private SensorPort portConnected;//Where is plugged in NXT Brick
	private int servoPosition = 0; //Position where Servo has been plugged
	private final byte SERVO1_POSITION = 0x5A; //The place where RC Servo has been plugged
	private final byte SERVO1_SPEED = 0x52;

	//Default Values
	private int angle = 0;
	private int pulse = 0;
	private int minAngle = 0;//Degree
	private int maxAngle = 180;//Degrees
	private int minPulse = 650;//500;//Ms
	private int maxPulse = 2350;//2500;//Ms

	/**
	 *
	 * The initial Constructor.
	 * This constructor establish where is plugged NXTServo on NXT Brick, 
	 * where the RC Servo is plugged into NXTServo
	 *
	 * @param port
	 * @param location
	 *
	 */
	public MServo(I2CPort port, int location){
		super(port);
		this.servoPosition = location;
	}

	/**
	 *
	 * The initial Constructor.
	 * This constructor establish where is plugged NXTServo on NXT Brick, 
	 * where the RC Servo is plugged into NXTServo
	 *
	 * @param port
	 * @param location
	 * @param servoName
	 *
	 */
	public MServo(I2CPort port, int location, String servoName){
		super(port);
		this.servoPosition = location;
	}
	
	/**
	 *
	 * Constructor with the feature to set min, max and init angle
	 *
	 * @param port
	 * @param location
	 * @param servoName
	 * @param min_angle
	 * @param max_angle
	 *
	 */
	public MServo(I2CPort port, int location, String servoName, int min_angle, int max_angle){
		this(port,location,servoName);
		
		this.minAngle = min_angle;
		this.maxAngle = max_angle;
	}

	/*
	 * Used to make a Linear Interpolation
	 * 
	 * From the HP Calculator idea:
	 * http://h10025.www1.hp.com/ewfrf/wc/fastFaqLiteDocument?lc=es&cc=mx&docname=bsia5214&dlc=es&product=20037
	 *
	 */
	private float getLinearInterpolation(float x,float x1, float x2, float y1, float y2){
		float y;
		y = ((y2-y1)/(x2-x1))*(x-x1) + y1;
		
		return y;
	}

	/**
	 * This method set the pulse in a RC Servo.
	 * 
	 * Note:Pulse range is: 500-2500, but internally
	 * it is necessary to divide into 2
	 * 
	 * @param pulse the pulse width
	 * 
	 */
	public void setPulse(int pulse){
		this.pulse = pulse;
		int internalPulse = Math.round(pulse/10);
		this.setAddress(MSC.NXTSERVO_ADDRESS);
		sendData(SERVO1_POSITION + servoPosition - 1, (byte)internalPulse);
	}
	
	/**
	 * Return the pulse used in last operation
	 * 
	 * @return the pulse
	 *
	 */
	public int getPulse(){
		return pulse;
	}

	/**
	 * Method to set an Angle in a RC Servo. 
	 * 
	 * @param angle the angle
	 * 
	 */
	public void setAngle(int angle){
		this.angle = angle;
		this.pulse = Math.round(getLinearInterpolation(angle,minAngle,maxAngle,minPulse,maxPulse));
		this.setPulse(pulse);
	}

	/**
	 * Return the angle used in last operation
	 * 
	 * @return the angle
	 *
	 */
	public int getAngle(){
		return angle;
	}

	/**
	 * Method to set the Speed in a RC Servo. 
	 * 
	 * @param speed the speed
	 * 
	 */
	public void setSpeed(int speed){
		this.setAddress(MSC.NXTSERVO_ADDRESS);
		sendData((SERVO1_SPEED + servoPosition - 1), (byte)speed);
	}
}
