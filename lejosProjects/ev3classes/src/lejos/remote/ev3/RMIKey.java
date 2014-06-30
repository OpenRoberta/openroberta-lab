package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.hardware.KeyListener;

public interface RMIKey extends Remote {
	
	public static final int UP = 0;
	public static final int ENTER = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int LEFT = 4;
	public static final int ESCAPE = 5;
	
	public int getId() throws RemoteException;
	
	public boolean isDown() throws RemoteException;
	
	public boolean isUp() throws RemoteException;
	
	public void waitForPress() throws RemoteException;
	
	public void waitForPressAndRelease() throws RemoteException;
	
	public void addKeyListener (KeyListener listener) throws RemoteException;
	
	public void simulateEvent(int event) throws RemoteException;
}
