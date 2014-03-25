package lejos.internal.io;

import java.io.IOException;
import java.io.OutputStream;

public class SocketOutputStream extends OutputStream {
	NativeDomainSocket sock;
	
	public SocketOutputStream(NativeDomainSocket sock) {
		this.sock = sock;
	}
	@Override
	public void write(int b) throws IOException {
		System.out.println("Not Writing " + b + ((char) b));
	}
	
   public void write(byte[] b, int off, int len) throws IOException
   {
	  //System.out.println("Writing len = " + len + " " + b[0]);
      sock.send(b, len);
   }
   
   public void write(byte [][] b) throws IOException {
	   System.err.println("Not implemented");
   }
}
