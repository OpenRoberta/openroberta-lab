package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMILED extends Remote {
	
	public void setPattern(int pattern) throws RemoteException;

}
