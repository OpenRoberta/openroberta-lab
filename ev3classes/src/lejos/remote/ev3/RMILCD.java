package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMILCD extends Remote {
	
	public void drawChar(char c, int x, int y) throws RemoteException;
	
	public void clearDisplay() throws RemoteException;
	
	public void drawString(String str, int x, int y, boolean inverted) throws RemoteException;
	 
	public void drawString(String str, int x, int y) throws RemoteException;
	 
	public void drawInt(int i, int x, int y) throws RemoteException;
	 
	public void drawInt(int i, int places, int x, int y) throws RemoteException;

}
