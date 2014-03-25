package lejos.remote.nxt;

import java.io.IOException;
import java.io.InputStream;

/**
 * Extends InputStream for BlueTooth; implements available()
 * @author   Roger Glassey revised on june 23, 2007, modified for Bluetooth2
 */
public class NXTInputStream extends InputStream {
	private byte buf[];
	private int bufIdx = 0, bufSize = 0;
	private NXTConnection conn = null;
    
	NXTInputStream(NXTConnection conn, int buffSize)
	{
		this.conn = conn;
        buf = new byte[buffSize];
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int avail = bufSize - bufIdx;
		if (avail <= 0) {
			bufSize = conn.read(buf, buf.length);
			if (bufSize < -1) throw new IOException();
			if (bufSize <= 0) return -1;
			bufIdx = 0;
			avail = bufSize;
		}

		if (len > avail)
			len = avail;

		System.arraycopy(buf, bufIdx, b, off, len);
		bufIdx += len;

		return len;
	}

    /**
     * Returns one byte as an integer between 0 and 255.  
     * Returns -1 if the end of the stream is reached.
     * Does not return till some bytes are available.
     */
	@Override
	public int read() throws IOException
    {
	   if (bufIdx >= bufSize) bufSize = 0;
	   if (bufSize <= 0)
	   {
		   bufSize = conn.read(buf, buf.length);
           if (bufSize < -1) throw new IOException();
		   if (bufSize <= 0) {
			   System.out.println("End of file");
			   return -1;
		   }
		   bufIdx = 0;
	   }
       return buf[bufIdx++] & 0xFF;
	}
	
    /**
     * returns the number of bytes in the input buffer - can be read without blocking
     */
    @Override
	public int available() throws IOException
    {
       if (bufIdx >= bufSize) bufSize = 0;
       if (bufSize == 0) {
    	   bufIdx = 0;
    	   bufSize = conn.read(buf, buf.length, false);
           if (bufSize < -1) throw new IOException();
           if (bufSize < 0) bufSize = 0;
       }
       return bufSize - bufIdx;
    }
    
    /**
     * the stream is restored to its original state - ready to receive more data.
     */
    @Override
	public void close()
    { 
       bufIdx = 0;
       bufSize = 0;
    }
}
