package lejos.remote.nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class NXTConnection implements StreamConnection {

    /**
     * Lego Communications Protocol (<code>LCP</code>) I/O mode. The LCP is defined by The Lego Company to allow limited remote 
     * command control of a NXT brick. 
     * 
     * See the <a href="http://mindstorms.lego.com">Lego Mindstorms</a> Site. Look for the Bluetooth Developer Kit in Support |
     * Files | Advanced
     */
    public static final int LCP = 1;
    /**
     * <code>PACKET</code> I/O mode. This is default and  is probably the best mode to use if you are talking to a
     * NXT using the leJOS classes. Headers are included for each packet of data sent and received.
     */
    public static final int PACKET = 0;
    /**
     * <code>RAW</code> I/O mode. This mode is just that and omits any headers. It is used normally for connections to non-NXT 
     * devices such as cell phones, etc.
     */
    public static final int RAW = 2;
    
	@Override
	public DataInputStream openDataInputStream() {
		return new DataInputStream(openInputStream());
	}

	@Override
	public InputStream openInputStream() {
		return new NXTInputStream(this, 256);
	}

	@Override
	public abstract void close() throws IOException;

	@Override
	public DataOutputStream openDataOutputStream() {
		return new DataOutputStream(openOutputStream());
	}

	@Override
	public OutputStream openOutputStream() {
		return new NXTOutputStream(this, 256);
	}

	public abstract int read(byte[] buf, int length);

	public abstract int write(byte[] buffer, int numBytes);

	public abstract int read(byte[] buf, int length, boolean b);
}
