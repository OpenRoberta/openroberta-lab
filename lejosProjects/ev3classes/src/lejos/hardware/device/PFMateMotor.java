package lejos.hardware.device;

import lejos.robotics.DCMotor;

/**
 * Motor class for PFMate class
 * 
 * @author Michael Smith <mdsmitty@gmail.com>
 * 
 **/
public class PFMateMotor implements DCMotor {
	private PFMate receiver;
	private int operReg, speedReg;
	private byte [] buffer = new byte[1];
	private final static byte FLT = 0, FORWARD = 1, BACKWARD = 2, STOP = 3;
	private boolean moving = false;

	/**
	 * @param recever PFMate object reference
	 * @param operReg Motor register
	 * @param speedReg Speed Register
	 */
	PFMateMotor(PFMate recever, int operReg, int speedReg){
		this.receiver = recever;
		this.operReg = operReg;
		this.speedReg = speedReg;
	}
	
	//motor operations
	/**
	 * Floats the motor
	 */
	public void flt(){
		receiver.sendData(operReg, FLT);
		moving = false;
	}
	
	/**
	 * Runs the motor forward
	 *
	 */
	public void forward(){
		receiver.sendData(operReg, FORWARD);
		receiver.sendData(0x41, (byte)0x47);
		moving = true;
	}
	
	/**
	 * Runs the motor backward
	 *
	 */
	public void backward(){
		receiver.sendData(operReg, BACKWARD);
		receiver.sendData(0x41, (byte)0x47);
		moving = true;
	}
	
	/**
	 * Stops the Motor
	 *
	 */
	public void stop(){
		receiver.sendData(operReg, STOP);
		receiver.sendData(0x41, (byte)0x47);
		moving = false;
	}

	/**
	 * Sets the motors speed
	 * @param speed 1 = 7
	 */
	public void setSpeed(int speed){
		if(speed < 1) speed = 1;
		if (speed > 7) speed = 7;
		receiver.sendData(speedReg, (byte) speed);
		receiver.sendData(0x41, (byte)0x47);
	}
	
	/**
	 * returns the speed
	 * @return 1 - 7
	 */
	public int getSpeed(){
		receiver.getData(speedReg, buffer, 1);
		return buffer[0];
	}
	
	/**
	 * Determines if motor is floating this is based on what the receiver has in its registers
	 * @return boolean
	 */
	public boolean isFlt(){
		receiver.getData(operReg, buffer, 1);
		if(buffer[0]== FLT) return true;
		return false;
	}
	
	/**
	 * Determines if motor is moving forward this is based on what the receiver has in its registers
	 * @return boolean
	 */
	public boolean isForward(){
		receiver.getData(operReg, buffer, 1);
		if(buffer[0]== FORWARD) return true;
		return false;
	}
	
	/**
	 * Determines if motor is moving backwards this is based on what the receiver has in its registers
	 * @return boolean
	 */
	public boolean isBackward(){
		receiver.getData(operReg, buffer, 1);
		if(buffer[0]== BACKWARD) return true;
		return false;
	}
	
	/**
	 * Determines if motor is stopped this is based on what the receiver has in its registers
	 * @return boolean
	 */
	public boolean isStop(){
		receiver.getData(operReg, buffer, 1);
		if(buffer[0]== STOP) return true;
		return false;
	}
	
	public boolean isMoving() {
		return moving;
	}

    public void setPower(int power)
    {
        setSpeed((power*7)/100);
    }

    public int getPower()
    {
        return (getSpeed()*100)/7;
    }
}
