package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIWifi extends Remote {
	
	public String[] getAccessPointNames() throws RemoteException;
	
	public String getAccessPoint() throws RemoteException;

}
