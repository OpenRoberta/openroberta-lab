package lejos.hardware;

import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTCommConnector;

public class Bluetooth {
	public static NXTCommConnector getNXTCommConnector() {
		return new BTConnector();
	}
	
	public static LocalBTDevice getLocalDevice() {
		return new LocalBTDevice();
	}
}
