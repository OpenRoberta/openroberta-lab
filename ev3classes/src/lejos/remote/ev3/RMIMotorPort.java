package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.hardware.port.BasicMotorPort;

public interface RMIMotorPort extends Remote {
	
    /**
     * Low-level method to control a motor. 
     * 
     * @param power power from 0-100
     * @param mode defined in <code>BasicMotorPort</code>. 1=forward, 2=backward, 3=stop, 4=float.
     * @see BasicMotorPort#FORWARD
     * @see BasicMotorPort#BACKWARD
     * @see BasicMotorPort#FLOAT
     * @see BasicMotorPort#STOP
     */
    public void controlMotor(int power, int mode) throws RemoteException;


    /**
     * returns tachometer count
     */
    public  int getTachoCount() throws RemoteException;
    
    /**
     *resets the tachometer count to 0;
     */ 
    public void resetTachoCount() throws RemoteException;
    
    public void setPWMMode(int mode) throws RemoteException;
    
    public void close() throws RemoteException;
    
}
