package lejos.hardware;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lejos.hardware.ev3.LocalEV3;
import lejos.internal.ev3.EV3DeviceManager;
import lejos.remote.ev3.RemoteEV3;

public class BrickFinder {
	private static final int DISCOVERY_PORT = 3016;
	private static Brick defaultBrick, localBrick;
	private static final int MAX_DISCOVERY_TIME = 2000;
	private static final int MAX_PACKET_SIZE = 32;
	
	public static Brick getLocal() {
		if (localBrick != null) return localBrick;
		// Check we are running on an EV3
		EV3DeviceManager.getLocalDeviceManager();
		localBrick = LocalEV3.get();
		return localBrick;
	}
	
	public static Brick getDefault() {
		if (defaultBrick != null) return defaultBrick;
		try {
			// See if we are running on an EV3
			EV3DeviceManager.getLocalDeviceManager();
			defaultBrick =  LocalEV3.get();
			return defaultBrick;
		} catch (UnsupportedOperationException e) {
			try {
				BrickInfo[] bricks = discover();
				if (bricks.length > 0) {
					defaultBrick = new RemoteEV3(bricks[0].getIPAddress());
					return defaultBrick;
				} else throw new DeviceException("No EV3 brick found");
			} catch (Exception e1) {
			    throw new DeviceException("No brick found");
			}
		}
	}

	/**
	 * Search for available EV3s and populate table with results.
	 */
	public static BrickInfo[] discover() throws IOException {	
		DatagramSocket socket = null;
		
		try {
			Map<String,BrickInfo> ev3s = new HashMap<String,BrickInfo>();

			socket = new DatagramSocket(DISCOVERY_PORT);
			socket.setSoTimeout(MAX_DISCOVERY_TIME);
	        DatagramPacket packet = new DatagramPacket (new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
	
	        long start = System.currentTimeMillis();
	        
	        while ((System.currentTimeMillis() - start) < MAX_DISCOVERY_TIME) {
	            socket.receive (packet);
	            String message = new String(packet.getData(), "UTF-8");
	            String ip = packet.getAddress().getHostAddress();
	            ev3s.put(ip, new BrickInfo(message.trim(),ip,"EV3"));
	        }
	        
	        BrickInfo[] devices = new BrickInfo[ev3s.size()];
	        int i = 0;
	        
	        for(String ev3: ev3s.keySet()) {
	        	devices[i++] = ev3s.get(ev3);
	        }
	        
	        return devices;
	        
		} finally {
			if (socket != null) socket.close();
		}     
	}
	
	public static BrickInfo[] discoverNXT() {
		try {
			Collection<RemoteBTDevice> nxts = Bluetooth.getLocalDevice().search();
			BrickInfo[] bricks = new BrickInfo[nxts.size()];
			int i = 0;
			for(RemoteBTDevice d: nxts) {
				BrickInfo b = new BrickInfo(d.getName(), d.getAddress(), "NXT");
				bricks[i++] = b;
			}
			return bricks;
		} catch (Exception e) {
			throw new DeviceException("Error finding remote NXTs", e);
		}
	}
	
	public static void setDefault(Brick brick) {
		defaultBrick = brick;
	}
}
