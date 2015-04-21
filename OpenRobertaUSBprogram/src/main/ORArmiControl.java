package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import ora.rmi.ORArmiMenu;

public class ORArmiControl {

    private static ORArmiMenu menu;

    public ORArmiControl() {
        // default
    }

    public void connectToMenu() throws Exception {
        menu = (ORArmiMenu) Naming.lookup("//" + Main.brickIp + "/RemoteMenu");
        System.out.println("Connected to brick!");
    }

    public void disconnectFromMenu() {
        menu = null;
    }

    public void uploadFile(String programName) {
        if ( menu == null ) {
            return;
        }
        try {
            File file = new File(programName);
            FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            in.read(data);
            System.out.println("Uploading " + file.getName());
            menu.uploadFile("/home/lejos/programs/" + file.getName(), data);
            in.close();
        } catch ( IOException ioe ) {
            System.out.println("IOException uploading program");
        }
    }

    public String getlejosVersion() throws RemoteException {
        return menu.getVersion();
    }

    public String getORAversion() throws RemoteException {
        return menu.getORAversion();
    }

    public String getBrickname() throws RemoteException {
        return menu.getName();

    }

    public String getBatteryVoltage() throws RemoteException {
        return menu.getORAbattery();
    }

    public void setORAregistration(boolean status) throws RemoteException {
        menu.setORAregistration(status);

    }

    public void runORAprogram(String programName) throws RemoteException {
        menu.runORAprogram(programName);
    }
}
