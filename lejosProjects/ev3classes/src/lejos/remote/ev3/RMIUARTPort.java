package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIUARTPort extends Remote {

    /**
     * read a single byte from the device
     * @return the byte value
     */
    public byte getByte() throws RemoteException;

    /**
     * read a number of bytes from the device
     * @param vals byte array to accept the data
     * @param offset offset at which to store the data
     * @param len number of bytes to read
     */
    public void getBytes(byte [] vals, int offset, int len) throws RemoteException;

    /**
     * read a single short from the device.
     * @return the short value
     */
    public int getShort() throws RemoteException;
   
    /**
     * read a number of shorts from the device
     * @param vals short array to accept the data
     * @param offset offset at which to store the data
     * @param len number of shorts to read
     */
    public void getShorts(short [] vals, int offset, int len) throws RemoteException;

    /**
     * Get the string name of the specified mode.<p><p>
     * TODO: Make other mode data available.
     * @param mode mode to lookup
     * @return String version of the mode name
     */
    public String getModeName(int mode) throws RemoteException;

    /**
     * Return the current sensor reading to a string. 
     */
    public String toStringValue() throws RemoteException;

    /**
     * Initialise the attached sensor and set it to the required operating mode.<br>
     * Note: This method is not normally needed as the sensor will be initialised
     * when it is opened. However it may be of use if the sensor needs to be reset
     * or in other cases.
     * @param mode target mode
     * @return true if ok false if error
     */
    boolean initialiseSensor(int mode) throws RemoteException;

    /**
     * Reset the attached sensor. Following this the sensor must be initialised
     * before it can be used.
     */
    void resetSensor() throws RemoteException;
    
    void close() throws RemoteException;
    
    boolean setMode(int mode) throws RemoteException;
}
