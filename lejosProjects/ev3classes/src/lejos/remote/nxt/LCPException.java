package lejos.remote.nxt;

import java.io.IOException;

public class LCPException extends IOException
{
	private byte errorcode;
	
	public LCPException() {
		super();
	}

	public LCPException(String s) {
		super(s);
	}
	
	public LCPException(byte errorcode) {
		this(ErrorMessages.lcpErrorToString(errorcode));
		this.errorcode = errorcode;
	}
	
	public LCPException(String s, Throwable cause) {
		this(s);
		this.initCause(cause);
	}
	
	/**
	 * Returns error code, if this exception was caused by the NXT returned an LCP error.
	 * @return non-zero balue of there the NXT provided an error code or zero otherwise.
	 */
	public byte getErrorcode() {
		return this.errorcode;
	}
}
