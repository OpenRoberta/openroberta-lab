package lejos.ev3.startup;

/**
 * No Singleton Pattern but not recommended to use more than one. This is a
 * member of GraphicStartup.
 *
 * @author dpyka
 */
public class ORAhandler {

    private static boolean hasConnectionError = false;
    private static boolean isRegistered = false;
    private static boolean interrupt = false;

    private ORApushCmd pushCmd;
    private Thread serverCommunicator;

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
     * Start the brick server "push" communication. IndicatorThread ind from
     * GraphicStartup is needed (for hiding explicitly) when launching a user
     * program.
     *
     * @param serverBaseIP
     *        String The base IP like 192.168.56.1:1999
     * @param ind
     *        IndicatorThread Title bar of the brick (shows battery, title and
     *        status icons).
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
     * Disconnect the http connection while brick is waiting for registration to
     * cancel it by pressing "Escape" key. Fiend a way to deregister button
     * listener (not possible at the moment?!)
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
