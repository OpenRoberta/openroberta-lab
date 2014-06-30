package lejos.hardware.device;

import lejos.hardware.port.BasicMotorPort;

/**
 * Supports a motor connected to the Mindsensors RCX Motor Multiplexer
 * 
 * @author Lawrie Griffiths
 *
 */
public class RCXPlexedMotorPort implements BasicMotorPort {
	private RCXMotorMultiplexer plex;
	private int id;
	
	public RCXPlexedMotorPort(RCXMotorMultiplexer plex, int id) {
		this.plex = plex;
		this.id = id;
	}
	
	public void controlMotor(int power, int mode) {
		int mmMode = mode;
		if (mmMode == BasicMotorPort.FLOAT) mmMode = 0; // float
		int mmPower = (int) (power * 2.55f);
		if (mmMode == BasicMotorPort.STOP) {
			mmPower = 255; // Maximum breaking
		}
		plex.sendCommand(id, mmMode, mmPower);
	}
	
	public void setPWMMode(int mode) {
		// Not implemented
	}

    @Override
    public void close()
    {
        // not implemented
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public void setPinMode(int mode)
    {

    }
}
