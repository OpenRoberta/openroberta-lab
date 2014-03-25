package lejos.remote.nxt;

import java.io.*;

import lejos.hardware.Power;
import lejos.hardware.port.PortException;

/**
 * Battery readings from a remote NXT.
 */
public class RemoteNXTBattery implements Power, NXTProtocol {
	
	private NXTCommand nxtCommand;
	
	public RemoteNXTBattery(NXTCommand nxtCommand) {
		this.nxtCommand = nxtCommand;
	}
		
	/**
	 * The NXT uses 6 batteries of 1500 mV each.
	 * @return Battery voltage in mV. ~9000 = full.
	 */
	public int getVoltageMilliVolt() {
		/*
	     * calculation from LEGO firmware
	     */
		try {
			return nxtCommand.getBatteryLevel();
		} catch (IOException e) {
			throw new PortException(e);
		}
	}

	/**
	 * The NXT uses 6 batteries of 1.5 V each.
	 * @return Battery voltage in Volt. ~9V = full.
	 */
	public float getVoltage() {
	   return (float)(getVoltageMilliVolt() * 0.001);
	}

	@Override
	public float getBatteryCurrent() {
		throw new UnsupportedOperationException("Battery current not supported by the NXT");
	}

	@Override
	public float getMotorCurrent() {
		throw new UnsupportedOperationException("Motor current not supported by the NXT");
	}
}

