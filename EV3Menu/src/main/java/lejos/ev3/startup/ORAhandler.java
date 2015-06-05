package lejos.ev3.startup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * No Singleton Pattern but do not use more than one
 *
 * @author dpyka
 */
public class ORAhandler {

    private static boolean hasConnectionError = false;
    private static boolean isRegistered = false;
    private static boolean timeout = false;

    private ORApushCmd pushCmd;
    private Thread serverCommunicator;

    private static boolean restart = true;

    /**
     * Creates a control object for most of the Open Roberta Lab related
     * functionality.
     */
    public ORAhandler() {
        createRestartScript();
        createWifiStartupScript();
    }

    private void createRestartScript() {
        File file = new File("/home/roberta/restartmenu.sh");
        if ( !file.exists() ) {
            try {
                FileWriter fw = new FileWriter(file);
                fw.write("#!/bin/sh\n");
                fw.write("\n");
                fw.write("killall java\n");
                fw.write("cd /home/root/lejos/bin/utils\n");

                fw.write("jrun -jar EV3Menu.jar $(cat /etc/hostname) $(cat /home/root/lejos/version) &\n");
                fw.write("\n");
                fw.close();
            } catch ( IOException e ) {
                System.out.println("Error: cannot write restart script!");
            }
        }
    }

    private void createWifiStartupScript() {
        File file = new File("/home/roberta/startwlan0.sh");
        if ( !file.exists() ) {
            try {
                FileWriter fw = new FileWriter(file);
                fw.write("#!/bin/sh\n");
                fw.write("\n");
                fw.write("ifconfig wlan0 up\n");
                fw.write("\n");
                fw.close();
            } catch ( IOException e ) {
                System.out.println("Error: cannot write wlan0 script!");
            }
        }
    }

    private void resetState() {
        setRegistered(false);
        setConnectionError(false);
        setTimeout(false);
    }

    /**
     * Start the brick server "push" communication.
     *
     * @param serverBaseIP
     *        The base IP like 192.168.56.1:1999
     * @param token
     *        Token for client/ brick identification
     */
    public void startServerCommunicator(String serverBaseIP, String token) {
        resetState();
        this.pushCmd = new ORApushCmd(serverBaseIP, token);
        this.serverCommunicator = new Thread(this.pushCmd);
        this.serverCommunicator.start();
    }

    /**
     * Disconnect the http connection to ORA server.
     */
    public void disconnect() {
        setRegistered(false);
        if ( this.pushCmd.getHttpConnection() != null ) {
            this.pushCmd.getHttpConnection().disconnect();
        }
    }

    public static boolean isRegistered() {
        return ORAhandler.isRegistered;
    }

    public static void setRegistered(boolean isRegistered) {
        ORAhandler.isRegistered = isRegistered;
    }

    public static boolean hasConnectionError() {
        return hasConnectionError;
    }

    public static void setConnectionError(boolean hasConnectionError) {
        ORAhandler.hasConnectionError = hasConnectionError;
    }

    public static boolean hasTimeout() {
        return timeout;
    }

    public static void setTimeout(boolean timeout) {
        ORAhandler.timeout = timeout;
    }

    public static boolean isRestarted() {
        return restart;
    }

    public static void setRestarted(boolean restart) {
        ORAhandler.restart = restart;
    }
}