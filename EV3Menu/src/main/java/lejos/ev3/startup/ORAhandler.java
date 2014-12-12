package lejos.ev3.startup;

import lejos.ev3.startup.GraphicStartup.IndicatorThread;

import org.json.JSONObject;

/**
 * No Singleton Pattern but not recommended to use more than one. This is a
 * member of GraphicStartup.
 * 
 * @author dpyka
 */
public class ORAhandler {
    private final IndicatorThread ind = null;

    // brick data keywords
    public static final String KEY_BRICKNAME = "brickname";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_MACADDR = "macaddr";
    public static final String KEY_BATTERY = "battery";
    public static final String KEY_VERSION = "version";

    private static final JSONObject brickData = new JSONObject();

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
     * Returns the JSONObject brick data which will be send to the server.
     * 
     * @return JSONObejct brickdata
     */
    public static JSONObject getBrickData() {
        return brickData;
    }

    /**
     * Set one key/value pair in the brickdata JSONObject.
     * 
     * @param key
     *        String The key in the jsonobject (use predefined static keywords)
     * @param value
     *        String The value in the jsonobject
     */
    public static void setBrickData(String key, String value) {
        brickData.put(key, value);
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
    public void startServerCommunicator(String serverBaseIP, IndicatorThread ind) {
        setInterrupt(false);
        setRegistered(false);
        setConnectionError(false);
        this.pushCmd = new ORApushCmd(serverBaseIP, this.ind);
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
