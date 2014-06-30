package lejos.hardware;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.freedesktop.dbus.exceptions.DBusException;

import com.sun.jna.LastErrorException;

import lejos.internal.dbus.DBusBluez;
import lejos.internal.io.NativeHCI;

public class LocalBTDevice {
	private NativeHCI hci = new NativeHCI();
	private HashMap<String,String> knownDevices = new HashMap<String,String>();
	private Properties props = new Properties();
	private FileReader fr;
	private String userHome = System.getProperty( "user.home" );
	private String cacheFile = userHome + "/nxj.cache";
	private DBusBluez db; 
	
	public LocalBTDevice() {
		try {
			db = new DBusBluez();
			fr = new FileReader(cacheFile);
			props.load(fr);
		    Enumeration<?> e = (Enumeration<?>) props.propertyNames();

		    while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      if (key.startsWith("NXT_")) {
		    	  knownDevices.put(props.getProperty(key), key.substring(4));
		      }
		    }
		} catch (IOException e) {
			System.out.println("Failed to load nxj.cache: " + e);
		} catch (DBusException e1) {
			System.err.println("Failed to create DBusJava: " + e1);
		}
	}
	
	public Collection<RemoteBTDevice> search() throws IOException {
		try {
			Collection<RemoteBTDevice> results = hci.hciInquiry();
			for(RemoteBTDevice d: results) {
				System.out.println("Found " + d.getName());
				knownDevices.put(d.getName(), d.getAddress());
				props.setProperty("NXT_" + d.getAddress(), d.getName());
			}
			saveKnownDevices();
			return results;
		} catch (LastErrorException e) {
			throw(new IOException(e.getMessage()));
		}
	}
	
	public void setVisibility(boolean visible) throws IOException {
		try {
			hci.hciSetVisible(visible);
		} catch (LastErrorException e) {
			throw(new IOException(e.getMessage()));
		}
	}
	
	public boolean getVisibility() {
		return hci.hcigetVisible();
	}
	
	public static boolean isPowerOn() {
		return true;
	}
	
	public String getBluetoothAddress() {
		StringBuilder sb = new StringBuilder();
		for(int j=5;j>=0;j--) {
			String hex = Integer.toHexString(getDeviceInfo() .bdaddr[j] & 0xFF).toUpperCase();
			if (hex.length() == 1) sb.append('0');
			sb.append(hex);
			if (j>0) sb.append(':');
		}
		return sb.toString();
	}
	
	public String getFriendlyName() {
		return "EV3";
	}
	
	public NativeHCI.DeviceInfo getDeviceInfo() {
		return hci.hciGetDeviceInfo();
	}
	
	private void saveKnownDevices() {	
		try {
			props.store(new FileWriter(cacheFile), null);
		} catch (IOException e) {
			System.err.println("Failed to save properties file: " + e);
		}
		
	}
	
	public static byte[] getBDAddr(String addr) {
		byte[] bdaddr = new byte[6];
		
		for(int i=0;i<addr.length();i += 3) {
			byte b = Byte.parseByte(addr.substring(i,i+2), 16);
			bdaddr[5 - (i/3)] = b;
		}
		
		return bdaddr;
	}
	
	public void authenticate(String deviceAddress, String pin) {
		try {
			db.authenticateRemoteDevice(deviceAddress, pin);
		} catch (DBusException e) {
			System.err.println("Failed to authenticate remote device: " + e);
		}
	}
}
