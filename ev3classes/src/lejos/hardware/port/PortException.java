package lejos.hardware.port;

/**
 * Exception thrown when errors are detected accessing a port.
 * 
 * @author Lawrie Griffiths
 *
 */
public class PortException extends RuntimeException
{
	private static final long serialVersionUID = 3469971731990732986L;

	public PortException()
    {
    }

    public PortException(String message)
    {
        super (message);
    }

    public PortException(Throwable cause)
    {
        super (cause);
    }

    public PortException(String message, Throwable cause)
    {
        super (message, cause);
    }
}
