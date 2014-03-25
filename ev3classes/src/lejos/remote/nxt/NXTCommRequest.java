package lejos.remote.nxt;

import java.io.*;

/**
 * Interface that all NXTComm implementation classes must implement for low-level communication
 * with the NXT.
 *
 */
public interface NXTCommRequest {

	/**
	 * Close the connection
	 * @throws IOException
	 */
	public void close() throws IOException;
	
	/**
	 * Send an LCP message to the NXT and receive a reply
	 * 
	 * @param message the LCP message
	 * @param replyLen the reply length expected
	 * @return the reply
	 * @throws IOException
	 */
	public byte[] sendRequest(byte [] message, int replyLen) throws IOException;

}


