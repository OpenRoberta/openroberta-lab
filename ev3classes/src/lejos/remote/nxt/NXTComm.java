package lejos.remote.nxt;

import java.io.*;

/**
 * 
 * Initiates communication to a remote NXT. Used by NXTCommand
 * to implement the Lego Communications Protocol (LCP) over Bluetooth.
 *
 */
public class NXTComm implements NXTCommRequest {
	private NXTConnection con;
    private NXTCommConnector connector;
	byte[] buf = new byte[64];

    /**
     * Create an NXTComm object and define what type of communications it should
     * use by specifying the appropriate connector object.
     * @param connector
     */
	NXTComm(NXTCommConnector connector)
    {
        this.connector = connector;
    }

	public boolean open(String name, int mode) throws IOException {		
		con = connector.connect(name, mode);
		if (con == null) return false;
		
		return true;
	}
	
	private void sendData(byte [] data) throws IOException {
		if (con.write(data, data.length) < 0) throw new IOException();
	}
	
	private byte[] readData() throws IOException {	
		int len = 0;
		
		while (len == 0) len = con.read(buf, buf.length);
        if (len < 0) throw new IOException();
		byte [] data = new byte[len];
        System.arraycopy(buf, 0, data, 0, len);
		return data;

	}
	
	public byte[] sendRequest(byte [] message, int replyLen) throws IOException {
		sendData(message);
		if (replyLen == 0) return new byte[0];
		return readData();
	}
	
	public void close() throws IOException {
		con.close();
	}
}
