package de.fhg.iais.roberta.codegen.cebithack;
import lejos.remote.nxt.NXTConnection;


/**
 * Interface for the Bluetooth connection protocol.
 * 
 * @author Andrea Suckro, Sebastian Höffner
 */
public interface BluetoothCom {
    
    /**
     * Establishes an {@link NXTConnection} with the host.
     *  
     * @param host the host (mac address or device name)
     * @param timeOut timeout in seconds
     * @return the connection
     */
    NXTConnection establishConnectionTo(String host, int timeOut);
   
    /**
     * Awaits an {@link NXTConnection}.
     * 
     * @param timeOut timeout in seconds
     * @return the connection
     */
    NXTConnection waitForConnection(int timeOut);
    
    /**
     * Reads a message from the given <code>connection</code>.
     * 
     * @param connection the connection to read from
     * @return the message
     */
    String readMessage(NXTConnection connection);
    
    /**
     * Sends a message to the given <code>connection</code>.
     * 
     * @param connection the connection to send to
     * @param message the message
     */
    void sendTo(NXTConnection connection, String message);

}
