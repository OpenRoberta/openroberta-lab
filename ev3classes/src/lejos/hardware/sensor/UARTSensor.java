package lejos.hardware.sensor;

import lejos.hardware.port.Port;
import lejos.hardware.port.UARTPort;

/**
 * Base class for EV3 UART based sensors. UART sensor drivers should extend this class
 * @author andy
 *
 */
public class UARTSensor extends BaseSensor
{
    protected UARTPort port;
    protected int currentMode;
    

     /**
     * Standard constructor for a UARTSensor initialises things and places the 
     * device into mode 0.
     * @param port The port the sensor is attached to.
     */
    public UARTSensor(UARTPort port)
    {
        this(port, 0);
    }
    
    /**
    * Standard constructor for a UARTSensor initialises things and places the 
    * device into mode 0.
    * @param port The port the sensor is attached to.
    */
   public UARTSensor(Port port)
   {
       this(port, 0);
   }

    /**
     * Create the sensor object and switch to the selected mode
     * @param port The port the sensor is attached to.
     * @param mode Operating mode for the sensor.
     */
    public UARTSensor(UARTPort port, int mode)
    {
        this.port = port;
        if (!port.setMode(mode))
            throw new IllegalArgumentException("Invalid sensor mode");
        currentMode = mode;
    }
    
    /**
     * Create the sensor object and switch to the selected mode
     * @param port The port the sensor is attached to.
     * @param mode Operating mode for the sensor.
     */
    public UARTSensor(Port port, int mode)
    {
        this(port.open(UARTPort.class));
        if (!this.port.setMode(mode))
        {
            this.port.close();
            throw new IllegalArgumentException("Invalid sensor mode");
        }
        releaseOnClose(this.port);
    }
    

    /**
     * Switch to the selected mode (if not already in that mode) and delay for the
     * specified period to allow the sensor to settle in the new mode. <br>
     * A mode of -1 resets the sensor.
     * TODO: There really should be a better way to work out when the switch is
     * complete, if you don't wait though you end up reading data from the previous
     * mode.
     * @param newMode The mode to switch to.
     * @param switchDelay Time in mS to delay after the switch.
     */
    protected void switchMode(int newMode, long switchDelay)
    {
        if (currentMode != newMode)
        {
            if (newMode == -1)
                port.resetSensor();
            else if (!port.setMode(newMode))
                throw new IllegalArgumentException("Invalid sensor mode");
            currentMode = newMode;
            //Delay.msDelay(switchDelay);
        }
        
    }
}
