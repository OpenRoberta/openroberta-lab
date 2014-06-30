package lejos.hardware.port;

import java.io.Closeable;

/**
 * Basic interface for EV3 sensor ports.
 * TODO: Need to cleanup the setting of modes etc. should probably become
 * part of the open, with some sort of name/value string pairs.
 * @author andy
 *
 */
public interface IOPort extends Closeable   {
   
    /**
     * Close the port, the port can not be used after this call.
     */
    public void close();
    
    /**
     * Return the string representing this port
     * @return the port name
     */
    public String getName();
    
   /**
     * Set the port pins up ready for use.
     * @param mode The EV3 pin mode
     */
    public void setPinMode(int mode);
    
}