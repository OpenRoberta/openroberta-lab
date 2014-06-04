package lejos.remote.nxt;

import lejos.hardware.port.BasicSensorPort;
import lejos.hardware.port.IOPort;
import lejos.hardware.sensor.EV3SensorConstants;

/**
 * This class provides the base operations for remote NXT sensor ports.
 * 
 * @author Lawrie Griffiths
 *
 */
public abstract class RemoteNXTIOPort implements IOPort, BasicSensorPort, EV3SensorConstants
{
    protected int port = -1;
    protected int typ = -1;
    protected RemoteNXTPort ref;
    protected static byte [] dc = new byte[3*PORTS];
    protected int currentMode = 0;
    protected static RemoteNXTIOPort [][] openPorts = new RemoteNXTIOPort[RemoteNXTPort.MOTOR_PORT+1][PORTS];
    protected NXTCommand nxtCommand;
    
    public RemoteNXTIOPort(NXTCommand nxtCommand) {
    	this.nxtCommand = nxtCommand;
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public String getName()
    {
        return ref.getName();
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public int getMode()
    {
        return currentMode;
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getType()
    {
        return 0;
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean setMode(int mode)
    {
        currentMode = mode;
        return true;
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean setType(int type)
    {
        throw new UnsupportedOperationException("This operation is for legacy modes only");
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean setTypeAndMode(int type, int mode)
    {
        setType(type);
        setMode(mode);
        return true;
    }

    /**
     * Open the sensor port. Ensure that the port is only opened once.
     * @param typ The type of port motor/sensor
     * @param port the port number
     * @param ref the Port ref for this port
     * @return true if success
     */
    public boolean open(int typ, int port, RemoteNXTPort ref)
    {
        synchronized (openPorts)
        {
            if (openPorts[typ][port] == null)
            {
                openPorts[typ][port] = this;
                this.port = port;
                this.typ = typ;
                this.ref = ref;
                return true;
            }
            return false;
        }
    }
   
    /** {@inheritDoc}
     */    
    @Override
    public void close()
    {
        if (port == -1)
            throw new IllegalStateException("Port is not open");
        synchronized (openPorts)
        {
            openPorts[typ][port] = null;
            port = -1;
        }
    }
    
    
   /**
     * Set the port pins up ready for use.
     * @param mode The EV3 pin mode
     */
    public void setPinMode(int mode)
    {
    	// Nothing required on the NXT
    }

}
