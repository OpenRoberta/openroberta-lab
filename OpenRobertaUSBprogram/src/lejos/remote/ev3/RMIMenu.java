package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Taken from ev3classes
 * 
 * @author leJOS Group
 */
public interface RMIMenu extends Remote {

    public void runProgram(String programName) throws RemoteException;

    public void debugProgram(String programName) throws RemoteException;

    public void runSample(String programName) throws RemoteException;

    public void stopProgram() throws RemoteException;

    public boolean deleteFile(String fileName) throws RemoteException;

    public long getFileSize(String fileName) throws RemoteException;

    public String[] getProgramNames() throws RemoteException;

    public String[] getSampleNames() throws RemoteException;

    public boolean uploadFile(String fileName, byte[] contents) throws RemoteException;

    public byte[] fetchFile(String fileName) throws RemoteException;

    public String getSetting(String setting) throws RemoteException;

    public void setSetting(String setting, String value) throws RemoteException;

    public void deleteAllPrograms() throws RemoteException;

    public String getVersion() throws RemoteException;

    public String getMenuVersion() throws RemoteException;

    public String getName() throws RemoteException;

    public void setName(String name) throws RemoteException;

    public void configureWifi(String ssid, String pwd) throws RemoteException;

    public String getExecutingProgramName() throws RemoteException;

    public void shutdown() throws RemoteException;

    public void suspend() throws RemoteException;

    public void resume() throws RemoteException;

}