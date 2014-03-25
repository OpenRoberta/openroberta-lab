package lejos.remote.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnection extends NXTConnection {
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	public SocketConnection(Socket socket) {
		this.socket = socket;
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (IOException e) {
			// Ignore for the moment
		}
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@Override
	public int read(byte[] buf, int length) {
		try {
			return is.read(buf, 0, length);
		} catch (IOException e) {
			return -2;
		}
	}

	@Override
	public int write(byte[] buffer, int numBytes) {
		try {
			os.write(buffer, 0, numBytes);
			return 0;
		} catch (IOException e) {
			return -2;
		}
	}

	@Override
	public int read(byte[] buf, int length, boolean b) {
		return read(buf,length);
	}
}
