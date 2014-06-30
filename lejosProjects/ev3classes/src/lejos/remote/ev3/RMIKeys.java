package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIKeys extends Remote {
	
	public void discardEvents() throws RemoteException;
	
	public int waitForAnyEvent() throws RemoteException;
	
	public int waitForAnyEvent(int timeout) throws RemoteException;
	
	public int waitForAnyPress(int timeout) throws RemoteException;
	
	public int waitForAnyPress() throws RemoteException;
	
	public int getButtons() throws RemoteException;
	
	public int readButtons() throws RemoteException;
	
	public void setKeyClickVolume(int vol) throws RemoteException;
	
	public int getKeyClickVolume() throws RemoteException;
	
	public void setKeyClickLength(int len) throws RemoteException;
	
	public int getKeyClickLength() throws RemoteException;
	
	public void setKeyClickTone(int key, int freq) throws RemoteException;
	
	public int getKeyClickTone(int key) throws RemoteException;

}
