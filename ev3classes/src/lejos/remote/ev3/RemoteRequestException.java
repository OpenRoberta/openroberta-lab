package lejos.remote.ev3;

/**
 * Exception thrown when errors are detected in a sensor device state.
 * @author andy
 *
 */
public class RemoteRequestException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 5846698127613306496L;

    public RemoteRequestException()
    {
    }

    public RemoteRequestException(String message)
    {
        super (message);
    }

    public RemoteRequestException(Throwable cause)
    {
        super (cause);
    }

    public RemoteRequestException(String message, Throwable cause)
    {
        super (message, cause);
    }
}
