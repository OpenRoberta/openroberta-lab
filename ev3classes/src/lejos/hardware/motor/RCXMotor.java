package lejos.hardware.motor;

import lejos.hardware.motor.BasicMotor;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.Port;

/**
 * Abstraction for an RCX motor.
 * 
 */
public class RCXMotor extends BasicMotor {
    
	public RCXMotor(BasicMotorPort port)
	{
		this.port = port;
	}
	
	public RCXMotor(Port port)
	{
	    this(port.open(BasicMotorPort.class));
	    releaseOnClose(this.port);
	}

}
