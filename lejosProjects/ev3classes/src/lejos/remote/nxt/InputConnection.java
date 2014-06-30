package lejos.remote.nxt;

import java.io.*;

public interface InputConnection extends Connection {
	/**
	 * Open and return a data input stream for a connection.
	 * @return the data input stream
	 */ 
	public DataInputStream openDataInputStream();
    
	/**
	 * Open and return an input stream for a connection.
	 * @return the input stream
	 */
	public InputStream openInputStream();
     
}
