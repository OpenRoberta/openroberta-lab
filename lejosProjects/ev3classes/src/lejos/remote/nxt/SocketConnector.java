package lejos.remote.nxt;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnector extends NXTCommConnector {

	@Override
	public NXTConnection connect(String target, int mode) {
		try {
			return new SocketConnection(new Socket(target,8888));
		} catch (IOException e) {
			System.err.println("Exception connecting to " + target + ": " + e);
			return null;
		}
	}

	@Override
	public NXTConnection waitForConnection(int timeout, int mode) {
		try {
			ServerSocket ss = new ServerSocket(8888);
			return new SocketConnection(ss.accept());
		} catch (IOException e) {
			System.err.println("Exception waiting for connection: " + e);
			return null;
		}
	}

	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}

}
