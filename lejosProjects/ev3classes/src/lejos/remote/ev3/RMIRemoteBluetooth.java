package lejos.remote.ev3;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import lejos.hardware.Bluetooth;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.RemoteBTDevice;

public class RMIRemoteBluetooth extends UnicastRemoteObject implements RMIBluetooth {
	private static final long serialVersionUID = 6508261990932121690L;
	private LocalBTDevice blue = Bluetooth.getLocalDevice();

	protected RMIRemoteBluetooth() throws RemoteException {
		super(0);
	}

	@Override
	public Collection<RemoteBTDevice> search() throws RemoteException {
		try {
			return blue.search();
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String getBluetoothAddress() throws RemoteException {
		return blue.getBluetoothAddress();
	}

	@Override
	public boolean getVisibility() throws RemoteException {
		return blue.getVisibility();
	}

	@Override
	public void setVisibility(boolean visible) throws RemoteException {
		try {
			blue.setVisibility(visible);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage());
		}		
	}


}
