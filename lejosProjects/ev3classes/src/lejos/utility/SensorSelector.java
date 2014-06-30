package lejos.utility;

import lejos.hardware.device.IRLink;
import lejos.hardware.device.IRTransmitter;
import lejos.hardware.device.RCXLink;
import lejos.hardware.port.I2CPort;
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.I2CSensor;
import lejos.hardware.sensor.MindsensorsAccelerometer;
import lejos.hardware.sensor.SensorMode;

/**
 * Factory for I2C sensor implementations.
 * Tests what make of sensor is connected to a port and creates 
 * an instance of the appropriate class for a given sensor interface.
 * 
 * @author Lawrie Griffiths
 *
 */
public class SensorSelector {
	
	private static final String MINDSENSORS_ID = "mndsnsrs";
	private static final String HITECHNIC_ID = "hitechnc";
		
	public static SensorMode createAccelerometer(I2CPort port) throws SensorSelectorException {
		I2CSensor tester = new I2CSensor(port);
		String type = tester.getVendorID().toLowerCase();
		
		if (type.equals(MINDSENSORS_ID))
			return new MindsensorsAccelerometer(port).getAccelerationMode();
		if (type.equals(HITECHNIC_ID))
			return new HiTechnicAccelerometer(port).getAccelerationMode();
		
		throw new SensorSelectorException("No Such Sensor");	
	}
	
	public static IRTransmitter createIRTransmitter(I2CPort port) throws SensorSelectorException {
		I2CSensor tester = new I2CSensor(port);
		String type = tester.getVendorID().toLowerCase();
		
		if (type.equals(MINDSENSORS_ID))
			return new RCXLink(port);
		if (type.equals(HITECHNIC_ID))
			return new IRLink(port);
		
		throw new SensorSelectorException("No Such Sensor");	
	}		
}
