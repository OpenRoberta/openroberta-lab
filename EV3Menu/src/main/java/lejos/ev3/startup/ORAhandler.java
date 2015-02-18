package lejos.ev3.startup;

/**
 * No Singleton Pattern but do not use
 * 
 * @author dpyka
 */
public class ORAhandler {

    private static boolean hasConnectionError = false;
    private static boolean isRegistered = false;
    private static boolean interrupt = false;

    private ORApushCmd pushCmd;
    private Thread serverCommunicator;

    public static boolean updated_without_restart = false;

    /**
     * Creates a control object for most of the Open Roberta Lab related
     * functionality.
     */
    public ORAhandler() {
        setInterrupt(false);
        setRegistered(false);
        setConnectionError(false);
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
        setInterrupt(false);
        setRegistered(false);
        setConnectionError(false);
        this.pushCmd = new ORApushCmd(serverBaseIP, token);
        this.serverCommunicator = new Thread(this.pushCmd);
        this.serverCommunicator.start();
    }

    /**
     * Disconnect the http connection to ORA server.
     */
    public void disconnect() {
        setInterrupt(true);
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

    public static boolean isInterrupt() {
        return interrupt;
    }

    public static void setInterrupt(boolean interrupt) {
        ORAhandler.interrupt = interrupt;
    }
}