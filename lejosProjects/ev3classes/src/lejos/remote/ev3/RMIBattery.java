package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIBattery extends Remote {

	/**
	 * The NXT uses 6 batteries of 1500 mV each.
	 * @return Battery voltage in mV. ~9000 = full.
	 */
	public int getVoltageMilliVolt() throws RemoteException;

	/**
	 * The NXT uses 6 batteries of 1.5 V each.
	 * @return Battery voltage in Volt. ~9V = full.
	 */
	public float getVoltage() throws RemoteException;

	/**
	 * Return the current draw from the battery
	 * @return current in Amps
	 */
	public float getBatteryCurrent() throws RemoteException;

	/**
	 * return the motor current draw
	 * @return current in Amps
	 */
	public float getMotorCurrent() throws RemoteException;
}
