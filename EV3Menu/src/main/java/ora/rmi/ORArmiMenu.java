package ora.rmi;

import java.rmi.RemoteException;

import lejos.remote.ev3.RMIMenu;

public interface ORArmiMenu extends RMIMenu {

    public String getORAversion() throws RemoteException;

    public String getORAbattery() throws RemoteException;

    public void setORAregistration(boolean status) throws RemoteException;

    public void runORAprogram(String programName) throws RemoteException;

}
