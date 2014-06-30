package lejos.remote.nxt;

/**
 * Standard interface to connect/wait for a connection.
 * @author andy
 */
public abstract class  NXTCommConnector {
    /**
     * Open a connection to the specified name/address using the given I/O mode
     * @param target The name or address of the device/host to connect to.
     * @param mode The I/O mode to use for this connection
     * @return A NXTConnection object for the new connection or null if error.
     */
    public abstract NXTConnection connect(String target, int mode);

    /**
     * Wait for an incoming connection, or for the request to timeout.
     * @param timeout Time in ms to wait for the connection to be made
     * @param mode I/O mode to be used for the accepted connection.
     * @return A NXTConnection object for the new connection or null if error.
     */
    public abstract NXTConnection waitForConnection(int timeout, int mode);

    /**
     * Cancel a connection attempt.
     * @return true if the connection attempt has been aborted.
     */
    public abstract boolean cancel();

}
