package lejos.remote.nxt;

import java.io.IOException;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.PortException;
import lejos.hardware.sensor.EV3SensorConstants;
import lejos.hardware.sensor.SensorConstants;

/**
 * This class provides access to the EV3 Analog based sensor ports and other
 * analog data sources.
 * 
 * @author Lawrie Griffiths
 *
 */
public class RemoteNXTAnalogPort extends RemoteNXTIOPort implements AnalogPort
{
    //TODO: If we ever implement reading of pin 6 on the NXT or return color sensor data
    // we must ensure that the correct reference value is used. This should be 3.3V
    // Note that even with this the readings for the color sensor may be slightly off
    // due to the supply voltage on the NXT being 4.5V v the EV3 5.0V. We may want
    // to correct for this.
	public RemoteNXTAnalogPort(NXTCommand nxtCommand) {
		super(nxtCommand);
	}

	private int id;
	private int type, mode;

    // The following method provide compatibility with NXT sensors
    
	/**
	 * Set the sensor type and mode
	 * @param type the sensor type
	 * @param mode the sensor mode
	 * @return 
	 */
	public boolean setTypeAndMode(int type, int mode) {
		this.type = type;
		this.mode = mode;
		try {
			nxtCommand.setInputMode(id, type, mode);
		} catch (IOException e) {
			throw new PortException(e);
		}
		return true;
	}
	
	/**
	 * Set the sensor type
	 * @param type the sensor type
	 * @return 
	 */
	public boolean setType(int type) {
		this.type = type;
		setTypeAndMode(type, mode);
		return true;
	}
	
	/**
	 * Set the sensor mode
	 * @param mode the sensor mode
	 * @return 
	 */
	public boolean setMode(int mode) {
		this.mode = mode;
		setTypeAndMode(type, mode);
		return true;
	}
	
    
    /**
     * get the type of the port
     * @param port
     * @return
     */
    public static int getPortType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return 0;
    }

    private int readRawValue()
    {
        try {
            InputValues vals = nxtCommand.getInputValues(id);
            return vals.rawADValue;
        } catch (IOException e) {
            throw new PortException(e);
        }
    }

    /**
     * Get the type of analog sensor (if any) attached to the port
     * @param port
     * @return
     */
    public static int getAnalogSensorType(int port)
    {
        if (port > PORTS || port < 0)
            return CONN_ERROR;
        return 0;
    }

	@Override
	public void getFloats(float[] vals, int offset, int length) {
		throw new UnsupportedOperationException("Not supported for a remote NXT");
	}

	@Override
	public float getPin6() {
        throw new UnsupportedOperationException("Not supported for a remote NXT");
	}

	@Override
	public float getPin1() {
        return (float)readRawValue()*EV3SensorConstants.ADC_REF/SensorConstants.NXT_ADC_RES;
	}
}
