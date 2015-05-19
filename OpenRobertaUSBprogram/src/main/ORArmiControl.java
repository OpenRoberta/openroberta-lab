package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import ora.rmi.ORArmiMenu;
import connectionEV3.USBConnector;

/**
 * Map methods to RMI calls.
 *
 * @author dpyka
 */
public class ORArmiControl {

    private final String brickLibDir = "/home/roberta/lib";
    private final String brickMenuDir = "/home/root/lejos/bin/utils";

    private final String brickIp;

    private boolean status = true;

    private static ORArmiMenu menu;
    private static Logger log = Logger.getLogger("Connector");

    public ORArmiControl(String brickIp) {
        this.brickIp = brickIp;
    }

    /**
     * Search for the RMI server of the EV3 and connect to the menu.
     *
     * @throws Exception
     */
    public void connectToMenu() throws Exception {
        menu = (ORArmiMenu) Naming.lookup("//" + this.brickIp + "/RemoteMenu");
    }

    /**
     * Set menu object to null. Brick does not know about connecting or disconnecting.
     */
    public void disconnectFromMenu() {
        menu = null;
    }

    /**
     * Upload the Open Robert Lab user program to the brick.
     *
     * @param programName
     */
    public void uploadFile(File file) {
        if ( menu == null ) {
            return;
        }
        try {
            //File file = new File(programName);
            FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            in.read(data);
            menu.uploadFile("/home/lejos/programs/" + file.getName(), data);
            in.close();
        } catch ( IOException ioe ) {
            log.severe("IOException uploading program " + ioe);
        }
    }

    /**
     * Upload all firmware files from the PC to the EV3.<br>
     * - OpenRobertaRuntime.jar<br>
     * - OpenRobertaShared.jar<br>
     * - json.jar<br>
     * - EV3Menu.jar
     *
     * @return True if writing all files was successful. False if at least one file has failed
     */
    public boolean uploadFirmwareFiles() {
        this.status = true;
        uploadFirmwareFile(this.brickLibDir, new File(USBConnector.TEMPDIRECTORY, "OpenRobertaRuntime.jar").getAbsolutePath());
        uploadFirmwareFile(this.brickLibDir, new File(USBConnector.TEMPDIRECTORY, "OpenRobertaShared.jar").getAbsolutePath());
        uploadFirmwareFile(this.brickLibDir, new File(USBConnector.TEMPDIRECTORY, "json.jar").getAbsolutePath());
        uploadFirmwareFile(this.brickMenuDir, new File(USBConnector.TEMPDIRECTORY, "EV3Menu.jar").getAbsolutePath());
        return this.status;
    }

    /**
     * Upload a file to the brick.
     *
     * @param directory in which the file is stored
     * @param fileName name + file extension
     */
    private void uploadFirmwareFile(String directory, String fileName) {
        if ( menu == null ) {
            this.status = this.status && false;
        }
        File file = new File(fileName);
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            in.read(data);
            log.info("Upload firmware file " + file.getName() + " successful!");
            menu.uploadFile(directory + "/" + file.getName(), data);
            in.close();
            this.status = this.status && true;
        } catch ( IOException ioe ) {
            log.severe("Error uploading firmware file" + file.getName() + " " + ioe);
            this.status = this.status && false;
        }
    }

    /**
     * Block all Open Roberta Lab functionality until the brick is restarted to prevent bugs after updating the firmware.
     *
     * @throws RemoteException
     */
    public void setORAupdateState() throws RemoteException {
        menu.setORAupdateState();
    }

    /**
     * Check if the EV3 firmware is updated, but not yet restarted.
     *
     * @return True if restarted, false if not.
     * @throws RemoteException
     */
    public boolean getORAupdateState() throws RemoteException {
        return menu.getORAupdateState();
    }

    /**
     * Get the leJOS version from the EV3.
     *
     * @return leJOS version
     * @throws RemoteException
     */
    public String getlejosVersion() throws RemoteException {
        return menu.getVersion();
    }

    /**
     * Get the Open Roberta Lab version from the EV3.
     *
     * @return ORA version
     * @throws RemoteException
     */
    public String getORAversion() throws RemoteException {
        return menu.getORAversion();
    }

    /**
     * Get the name of the EV3.
     *
     * @return brickname
     * @throws RemoteException
     */
    public String getBrickname() throws RemoteException {
        return menu.getName();

    }

    /**
     * Get the battery voltage from the EV3.
     *
     * @return battery voltage as string (for example 8.0)
     * @throws RemoteException
     */
    public String getBatteryVoltage() throws RemoteException {
        return menu.getORAbattery();
    }

    /**
     * Notify the EV3 when connecting to/ disconnecting from Open Roberta Lab.
     *
     * @param status True if connecting. False if disconnecting
     * @throws RemoteException
     */
    public void setORAregistration(boolean status) throws RemoteException {
        menu.setORAregistration(status);

    }

    /**
     * Execute a Open Roberta Lab user program on the brick.
     *
     * @param programName name of the program
     * @throws RemoteException
     */
    public void runORAprogram(String programName) throws RemoteException {
        menu.runORAprogram(programName);
    }
}
