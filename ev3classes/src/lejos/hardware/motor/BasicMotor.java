package lejos.hardware.motor;

import lejos.hardware.Device;
import lejos.hardware.port.BasicMotorPort;
import lejos.robotics.DCMotor;

/** 
 * Abstraction for basic motor operations.
 * 
 * @author Lawrie Griffiths.
 *
 */
public abstract class BasicMotor extends Device implements DCMotor
{
    protected static int INVALID_MODE = -1;
	protected int mode = INVALID_MODE;
    protected BasicMotorPort port;
    protected int power = 0;

    public void setPower(int power)
    {
        this.power = power;
        port.controlMotor(power, mode);
    }

    public int getPower()
    {
        return power;
    }

    /**
     * Update the internal state tracking the motor direction
     * @param newMode
     */
    protected void updateState( int newMode)
    {
        if (newMode == mode) return;
        mode = newMode;
        port.controlMotor(power, newMode);
    }

	/**
	 * Causes motor to rotate forward.
	 */
	public void forward()
	{ 
		updateState( BasicMotorPort.FORWARD);
	}
	  

	/**
	 * Causes motor to rotate backwards.
	 */
	public void backward()
	{
		updateState( BasicMotorPort.BACKWARD);
	}


	/**
	 * Returns true iff the motor is in motion.
	 * 
	 * @return true iff the motor is currently in motion.
	 */
	public boolean isMoving()
	{
		return (mode == BasicMotorPort.FORWARD || mode == BasicMotorPort.BACKWARD);
	}

	/**
	 * Causes motor to float. The motor will lose all power,
	 * but this is not the same as stopping. Use this
	 * method if you don't want your robot to trip in
	 * abrupt turns.
	 */   
	public void flt()
	{
		updateState( BasicMotorPort.FLOAT);
	}

	  
	/**
	 * Causes motor to stop, pretty much
	 * instantaneously. In other words, the
	 * motor doesn't just stop; it will resist
	 * any further motion.
	 * Cancels any rotate() orders in progress
	 */
	public void stop()
	{
		updateState( BasicMotorPort.STOP);
	}
}

