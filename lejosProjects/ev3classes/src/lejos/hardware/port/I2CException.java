package lejos.hardware.port;

public class I2CException extends PortException  {
	
	private static final long serialVersionUID = -1429576101467185066L;

	public I2CException()
    {
    }

    public I2CException(String message)
    {
        super (message);
    }

    public I2CException(Throwable cause)
    {
        super (cause);
    }

    public I2CException(String message, Throwable cause)
    {
        super (message, cause);
    }
}
