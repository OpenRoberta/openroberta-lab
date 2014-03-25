package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import lejos.hardware.LocalWifiDevice;
import lejos.hardware.Wifi;

public class RMIRemoteWifi  extends UnicastRemoteObject implements RMIWifi {
	private static final long serialVersionUID = -3707578706658225092L;
	private LocalWifiDevice wifi = Wifi.getLocalDevice("wlan0");

	protected RMIRemoteWifi() throws RemoteException {
		super(0);
	}

	@Override
	public String[] getAccessPointNames() throws RemoteException {
		return wifi.getAccessPointNames();
	}

	@Override
	public String getAccessPoint() throws RemoteException {
		return wifi.getAccessPoint();
	}
}
