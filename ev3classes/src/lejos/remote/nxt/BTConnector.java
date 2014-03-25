package lejos.remote.nxt;

import java.io.IOException;
import java.util.Collection;

import com.sun.jna.LastErrorException;

import lejos.hardware.Bluetooth;
import lejos.hardware.RemoteBTDevice;
import lejos.internal.io.NativeHCI;
import lejos.internal.io.NativeSocket;

public class BTConnector extends NXTCommConnector  {
	NativeSocket socket = new NativeSocket(NativeHCI.AF_BLUETOOTH, NativeHCI.SOCK_STREAM, NativeHCI.BTPROTO_RFCOMM);
	
	public BTConnection connect(String target, int mode) {
		NativeSocket.SockAddr sa = new NativeSocket.SockAddr();
		
		if (target != null && target.length() == 17 && target.contains(":")) {
			// A device address given, convert to internal format
			for(int i=0;i<6;i++) {
				byte b = (byte) (int) Integer.valueOf(target.substring(i*3,i*3+2), 16);
				sa.bd_addr[5-i] = b;
			}
			
			try {
				socket.connect(sa, sa.size());
			} catch (LastErrorException e) {
				if (e.getErrorCode() == 112) System.err.println("Error connecting to " + target + ": Host is down");
				else System.err.println("Error connecting to " + target + ": " + e);
				return null;
			}
			
			return new BTConnection(socket, mode);
		} else {
			
			try {
				Collection<RemoteBTDevice> devices = Bluetooth.getLocalDevice().search();
				RemoteBTDevice btDevice = null;
				
				if (devices == null) return null;
				
				for(RemoteBTDevice device: devices) {
				
					if (target == null || target.length() == 0 || target.equals("*")) {
						btDevice = device;
						break;
					} else if (device.getName().equals(target)) {
						btDevice = device;
						break;
					}
				}
				
				if (btDevice == null) return null;
				
				for(int i=0;i<6;i++) {
					sa.bd_addr[i] = btDevice.getDeviceAddress()[i];
				}
				
				socket.connect(sa, sa.size());
				
				return new BTConnection(socket, mode);
				
			} catch (IOException e) {
				System.err.println("Error getting remote devices: " + e);
				return null;
			}
		}
	}

	@Override
	public NXTConnection waitForConnection(int timeout, int mode) {
		NativeSocket.SockAddr sa = new NativeSocket.SockAddr();
		
		socket.bind(sa, sa.size());
		
		socket.listen(1);
		
		NativeSocket client = socket.accept();
		return new BTConnection(client, mode);
	}

	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}
}
