package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMISampleProvider extends Remote {
	public float[] fetchSample() throws RemoteException;
	
	public void close() throws RemoteException;
}
