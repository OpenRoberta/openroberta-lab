package lejos.remote.nxt;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Implements an OutputStream over NXT connections.
 *
 */
public class NXTOutputStream extends OutputStream {
	private byte[] buffer;
	private int numBytes = 0;
	private NXTConnection conn = null;
	
	NXTOutputStream(NXTConnection conn, int buffSize)
	{
		this.conn = conn;
        buffer = new byte[buffSize];
	}
	
    @Override
	public synchronized void write(int b) throws IOException {
    	if (numBytes >= buffer.length) {
    		flush();
    	}
    	buffer[numBytes] = (byte) b;
    	numBytes++;  	
    }
    
    //TODO implement write(byte[], int, int)
    
    @Override
	public synchronized void flush() throws IOException{
		if (numBytes > 0) {
			if (conn.write(buffer, numBytes) < 0) throw new IOException();
			numBytes = 0;
		}
	}
    
    @Override
    public void close() throws IOException {
    	this.flush();
    	//TODO mark stream closed
    }
}
