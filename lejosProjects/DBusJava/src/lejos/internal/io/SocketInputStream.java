package lejos.internal.io;

import java.io.IOException;
import java.io.InputStream;

public class SocketInputStream extends InputStream {
	private NativeDomainSocket sock;
	private byte[] buf = new byte[1];

	public SocketInputStream(NativeDomainSocket sock) {
		this.sock = sock;
	}
	
	@Override
	public int read() throws IOException {
		int count = sock.recv(buf, 1);
		//System.out.println("Read" + count + " " + buf[0] + " " + ((char) buf[0]));
		return buf[0] & 0xFF;
	}
	
	   public int read(byte[] b, int off, int len) throws IOException
	   {
	      int count = sock.recv(b, len);
	      /* Yes, I really want to do this. Recv returns 0 for 'connection shut down'.
	       * read() returns -1 for 'end of stream.
	       * Recv returns -1 for 'EAGAIN' (all other errors cause an exception to be raised)
	       * whereas read() returns 0 for '0 bytes read', so yes, I really want to swap them here.
	       */
	      if (0 == count) return -1;
	      else if (-1 == count) return 0;
	      else {
	    	  //System.out.println("Received " + count + " "  + b[0] + ((char) b[0]));
	    	  return count;
	      }
	   }

}
