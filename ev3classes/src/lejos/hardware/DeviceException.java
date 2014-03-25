package lejos.hardware;

/**
 * Exception thrown when errors are detected in a sensor device state.
 * @author andy
 *
 */
public class DeviceException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 5846698127613306496L;

    public DeviceException()
    {
    }

    public DeviceException(String message)
    {
        super (message);
    }

    public DeviceException(Throwable cause)
    {
        super (cause);
    }

    public DeviceException(String message, Throwable cause)
    {
        super (message, cause);
    }
}
