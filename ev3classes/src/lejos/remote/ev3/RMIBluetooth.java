package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import lejos.hardware.RemoteBTDevice;

public interface RMIBluetooth extends Remote {
	
	public Collection<RemoteBTDevice> search() throws RemoteException;
	
	public String getBluetoothAddress() throws RemoteException;
	
	public boolean getVisibility() throws RemoteException;
	
	public void setVisibility(boolean visible) throws RemoteException;

}
