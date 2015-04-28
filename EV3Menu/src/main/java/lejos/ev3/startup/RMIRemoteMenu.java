package lejos.ev3.startup;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ora.rmi.ORAmenu;
import ora.rmi.ORArmiMenu;

public class RMIRemoteMenu extends UnicastRemoteObject implements ORArmiMenu {
    private final ORAmenu menu;

    private static final long serialVersionUID = 9132686914626791288L;

    protected RMIRemoteMenu(ORAmenu menu) throws RemoteException {
        super(0);
        this.menu = menu;
    }

    @Override
    public void runProgram(String programName) throws RemoteException {
        this.menu.runProgram(programName);
    }

    @Override
    public boolean deleteFile(String fileName) throws RemoteException {
        return this.menu.deleteFile(fileName);
    }

    @Override
    public String[] getProgramNames() throws RemoteException {
        return this.menu.getProgramNames();
    }

    @Override
    public void debugProgram(String programName) throws RemoteException {
        this.menu.debugProgram(programName);
    }

    @Override
    public void runSample(String programName) throws RemoteException {
        this.menu.runSample(programName);
    }

    @Override
    public String[] getSampleNames() throws RemoteException {
        return this.menu.getSampleNames();
    }

    @Override
    public long getFileSize(String filename) {
        return this.menu.getFileSize(filename);
    }

    @Override
    public boolean uploadFile(String fileName, byte[] contents) throws RemoteException {
        return this.menu.uploadFile(fileName, contents);
    }

    @Override
    public byte[] fetchFile(String fileName) throws RemoteException {
        return this.menu.fetchFile(fileName);
    }

    @Override
    public String getSetting(String setting) throws RemoteException {
        return this.menu.getSetting(setting);
    }

    @Override
    public void setSetting(String setting, String value) throws RemoteException {
        this.menu.setSetting(setting, value);
    }

    @Override
    public void deleteAllPrograms() throws RemoteException {
        this.menu.deleteAllPrograms();
    }

    @Override
    public String getVersion() throws RemoteException {
        return this.menu.getVersion();
    }

    @Override
    public String getMenuVersion() throws RemoteException {
        return this.menu.getMenuVersion();
    }

    @Override
    public String getName() throws RemoteException {
        return this.menu.getName();
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.menu.setName(name);
    }

    @Override
    public void configureWifi(String ssid, String pwd) throws RemoteException {
        WPASupplicant.writeConfiguration("wpa_supplicant.txt", "wpa_supplicant.conf", ssid, pwd);
    }

    @Override
    public void stopProgram() throws RemoteException {
        this.menu.stopProgram();
    }

    @Override
    public String getExecutingProgramName() throws RemoteException {
        return this.menu.getExecutingProgramName();
    }

    @Override
    public void shutdown() throws RemoteException {
        this.menu.shutdown();
    }

    @Override
    public void suspend() throws RemoteException {
        this.menu.suspend();
    }

    @Override
    public void resume() throws RemoteException {
        this.menu.resume();
    }

    @Override
    public String getORAversion() throws RemoteException {
        return this.menu.getORAversion();
    }

    @Override
    public String getORAbattery() throws RemoteException {
        return this.menu.getORAbattery();
    }

    @Override
    public void setORAregistration(boolean status) throws RemoteException {
        this.menu.setORAregistration(status);

    }

    @Override
    public void runORAprogram(String programName) throws RemoteException {
        this.menu.runORAprogram(programName);

    }

    @Override
    public void setORAupdateState() throws RemoteException {
        this.menu.setORAupdateState();

    }
}
