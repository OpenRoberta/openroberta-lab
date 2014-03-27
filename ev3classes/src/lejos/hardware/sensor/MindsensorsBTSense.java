package lejos.hardware.sensor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import lejos.hardware.Bluetooth;
import lejos.hardware.sensor.SensorMode;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;

/**
 * Support for the Mindsensors BTSense application
 * 
 * @author Lawrie Griffiths
 *
 */
public class MindsensorsBTSense {
	private  int trId = 0;
	private InputStream is;
	private OutputStream os;
	private byte[] reply = new byte[512];
	private int currLen = 0;
	private int packets = 0;
	private boolean debug = true;
	
	// Map from character mode indicator to queue for the sensor mode
	private Map<Character, Queue<Float[]>> queues = new HashMap<Character,Queue<Float[]>>();
	private Map<Character, Float[]> latest = new HashMap<Character,Float[]>();
	
	/**
	 * A - Accelerometer
	 * G - Gravity
	 * N - Linear Acceleration
	 * L - Light
	 * M - Magnetic Field
	 * C - Compass
	 * P - Proximity
	 * S - Sound
	 * D - Date
	 * T - Time
	 * G - Gyroscope
	 * E - Atmospheric pressure
	 * H - Humidity
	 * U - Temperature
	 * W - Network location
	 * O - GPS Location
	 * 
	 */
	private char[] sensorTypes = {'A','R','N','L','M','C','P','S','T','D','G','E','H','U','W','O'};
	
	/**
	 * Connection to the BTSense application and identify device as EV3
	 * @throws IOException 
	 */
	public MindsensorsBTSense() throws IOException {
		NXTCommConnector connector = Bluetooth.getNXTCommConnector();
		
		System.out.println("Waiting for connection ...");
		NXTConnection con = connector.waitForConnection(0, NXTConnection.RAW);
		System.out.println("Connected");
		
        is = con.openInputStream();
        os = con.openOutputStream();
        
        System.out.println("Sending Identify command");
        
        sendCmd("@:xIE");
        
        // Create queues of sensor data for each potential sensor
        for (char c: sensorTypes) {
        	queues.put(c, new LinkedList<Float[]>());
        }
         
        // Start a background thread to read data from phone anbd put it in the relevant queue
        new DataReader().start();
	}
	
	/**
	 * Get a SensorMode object for a specific sensor mode and request data from phone
	 * 
	 * @param sensorType the sihngle character sensor mode identifier
	 * @return the SensorMode implementation
	 * @throws IOException
	 */
	public SensorMode getSensorMode(char sensorType) throws IOException {
		Queue<Float[]> queue = new LinkedList<Float[]>();
		
		queues.put(sensorType, queue);
		
		sendCmd("+:t" + sensorType + "1");
		
		return new BTSenseMode(sensorType, queue, latest);	
	}
	
	/**
	 * Send a command to the phone application, with a transaction id
	 * @param cmd
	 * @throws IOException
	 */
	public void sendCmd(String cmd) throws IOException {
		byte[] b = new byte[cmd.length()+2];
		b[0] = (byte) (cmd.length());
		b[1] = 0;
		System.arraycopy(cmd.getBytes(), 0, b, 2, cmd.length());
		b[4] = (byte) trId++;
		
    	os.write(b, 0, cmd.length()+2);	
    	os.flush();	
	}
	
	// Read the reply into the reply buffer at specified offset
	public int getReply(int offset) throws IOException {
		//System.out.println("Reading data at offset " + offset);
		return is.read(reply, offset, reply.length);
	}
	
	/**
	 * Process data from the phone application
	 * @throws IOException
	 */
	public void processData() throws IOException {
	    int len = 0;
	    
		while(true) {
			if (currLen == 0) { 
				len = getReply(0);
				System.out.println("Received " + len + " bytes ");
				if (len <= 0) return;
				currLen = len;
			}
			
			int packetLength = reply[0];
			//System.out.println("Packet length = " + packetLength);
			
			if (packetLength > currLen - 1) {
				len = getReply(currLen);
				System.out.println("Received extra " + len + " bytes ");
				if (len <= 0) return;
				currLen += len;
			}

			//printReply();

			byte [] packet = new byte[packetLength];
			System.arraycopy(reply, 1, packet, 0, packetLength);
			packets++;
			extractData(packet);
			
			System.arraycopy(reply, packetLength+1, reply, 0, currLen - (packetLength + 1));
			currLen -= (packetLength + 1);
		}
	}
	
	// Diagnostic method to print the reply data in hex
	private void printReply() {
		// Print the current data
		for(int i=0;i<currLen;i++) {
			String s = Integer.toHexString(reply[i] & 0xFF);
			System.out.print((s.length() == 1 ? "0" : "") + s + " ");
		}
		System.out.println();
	}
	
	/**
	 * Extract data from the Data reply message
	 * @param packet
	 * @throws UnsupportedEncodingException
	 */
	private void extractData(byte[] packet) throws UnsupportedEncodingException {		
		for(int i=0;i<5;i++) {
			int  location = (packet[15+i] & 0xFF);
			
			if (location == 0xff) break;
			char sensorType = (char) packet[13 + location];
			
			if (debug) System.out.print("Priority: " + ((char) packet[14]) + " Packet: " + packets + " ");
			processSensor(sensorType, packet, 14 + location);
		}	
	}		
	
	/*
	 * Process the data from for a specific sensor mode
	 */
	private void processSensor(char sensorType, byte[] packet, int offset) throws NumberFormatException, UnsupportedEncodingException {
		//System.out.println("Sensor type is " + sensorType);
		
		Float[] data = null;
		
		switch (sensorType) {
		case 'A':
			
			int x = getInt(packet,offset,2);
			int y = getInt(packet,offset+2,2);
			int z = getInt(packet,offset+4,2);
			
			if (debug) System.out.println("Acceleration X: " + x + " Y: " + y + " Z: " + z);

			data = new Float[3];
			data[0] = (float) x;
			data[1] = (float) y;
			data[2] = (float) z;
		
			break;
			
		case 'R':
			
			x = getInt(packet,offset,2);
			y = getInt(packet,offset+2,2);
			z = getInt(packet,offset+4,2);
			
			data = new Float[3];
			data[0] = (float) x;
			data[1] = (float) y;
			data[2] = (float) z;
			
			if (debug) System.out.println("Gravity X: " + x + " Y: " + y + " Z: " + z);
			break;
			
		case 'N':
			
			x = getInt(packet,offset,2);
			y = getInt(packet,offset+2,2);
			z = getInt(packet,offset+4,2);
			
			data = new Float[3];
			data[0] = (float) x;
			data[1] = (float) y;
			data[2] = (float) z;
			
			if (debug) System.out.println("Linear acceleration X: " + x + " Y: " + y + " Z: " + z);
			break;
			
		case 'M':
			
			x = getInt(packet,offset,2);
			y = getInt(packet,offset+2,2);
			z = getInt(packet,offset+4,2);
			
			data = new Float[3];
			data[0] = (float) x;
			data[1] = (float) y;
			data[2] = (float) z;
			
			if (debug) System.out.println("Magnetic X: " + x + " Y: " + y + " Z: " + z);
			break;

		case 'G':
			
			x = getInt(packet,offset,2);
			y = getInt(packet,offset+2,2);
			z = getInt(packet,offset+4,2);
			
			data = new Float[3];
			data[0] = (float) x;
			data[1] = (float) y;
			data[2] = (float) z;
			
			if (debug) System.out.println("Gyroscope X: " + x + " Y: " + y + " Z: " + z);
			break;
			
		case 'L':
			
			int light = getInt(packet,offset,2);
			
			data = new Float[1];
			data[0] = (float) light;
			
			if (debug) System.out.println("Light " + light);
			break;
			
		case 'S':
			
			int sound = getInt(packet,offset,2);
			
			
			data = new Float[1];
			data[0] = (float) sound;
			
			if (debug) System.out.println("Sound " + sound);
			break;
			
		case 'V':
			
			int version = getInt(packet,offset,2);
			
			
			data = new Float[1];
			data[0] = (float) version;
			
			if (debug) System.out.println("Version " + version);
			break;
			
		case 'P':
			
			int proximity = getInt(packet,offset,2);
			
			data = new Float[1];
			data[0] = (float) proximity;
			
			if (debug) System.out.println("Proximity " + proximity);
			break;
			
		case 'E':
			
			int pressure = getInt(packet,offset,2);
			
			data = new Float[1];
			data[0] = (float) pressure;
			
			if (debug) System.out.println("Atmospheric pressure " + pressure);
			break;
			
		case 'H':
			
			int humidity = getInt(packet,offset,2);
				
			data = new Float[1];
			data[0] = (float) humidity;
			
			if (debug) System.out.println("Humidity " + humidity);
			break;
			
		case 'C':
			
			if (debug) System.out.println("Compass mot implemented");
			break;
			
		case 'W':
			
			int lattitude = getInt(packet,offset,2);
			int longitude = getInt(packet,offset+2,2);
			
			data = new Float[2];
			data[0] = (float) lattitude;
			data[1] = (float) longitude;
			
			if (debug) System.out.println("Network location Lat: " + lattitude + " Long: " + longitude);
			break;
			
		case 'O':

			int hd = getInt(packet,offset,4);
			
			int hm = getInt(packet,offset+2,2);
			
			int hs = getInt(packet,offset+4,2);
			
			int wd = getInt(packet,offset,6);
			
			int wm = getInt(packet,offset+8,2);
			
			int ws = getInt(packet,offset+10,2);
			
			data = new Float[6];
			data[0] = (float) hd;
			data[1] = (float) hm;
			data[2] = (float) hs;
			data[0] = (float) wd;
			data[1] = (float) wm;
			data[2] = (float) ws;
			
			if (debug) System.out.println("GPS Lat: " + hd + ":" + hm + ":" + hs + " Long: " + wd + ":" + wm + ":" + ws);
			break;
			
		case 'D':

			int year = getInt(packet,offset,4);
			
			int month = getInt(packet,offset+4,2);
			
			int day = getInt(packet,offset+6,2);
			
			data = new Float[3];
			data[0] = (float) year;
			data[1] = (float) month;
			data[2] = (float) day;
			
			if (debug) System.out.println("Date is " + day + "/" + month + "/" + year);
			break;
			
		case 'T':
			int millis = getInt(packet,offset,8);
			
			int secs = millis /1000;
			
			int hours = secs/3600;
			
			secs -= (hours * 3600);
			
			int minutes = secs/60;
			
			secs -= (minutes * 60);
			
			data = new Float[3];
			data[0] = (float) hours;
			data[1] = (float) minutes;
			data[2] = (float) secs;
			
			if (debug) System.out.println("Time is " + hours + ":" + minutes + ":" + secs);
			break;
		}
		
		if (data != null) {
			queues.get(sensorType).add(data);
			latest.put(sensorType, data);
		}
	}
	
	/*
	 * Get an integer in ascii hex encoding from the data packet
	 */
	private int getInt(byte[] packet, int offset, int len) throws NumberFormatException, UnsupportedEncodingException {
		byte[] b = new byte[len];
		System.arraycopy(packet, offset, b, 0, len);
		return Integer.parseInt(new String(b,"UTF-8"), 16);
	}
	
	/*
	 * Background thread to process the data from the phone
	 */
	class DataReader extends Thread {
		
		public DataReader() {
			setDaemon(false);
		}
		
		@Override
		public void run() {
			try {
				processData();
			} catch (IOException e) {
				System.err.println("IOException in processCommands: " + e);
			}
		}
	}
	
	/*
	 * Class that implements a SensorMode for the specific sensor
	 */
	static class BTSenseMode implements SensorMode {
		private char sensorType;
		private int sampleSize;
		private Queue<Float[]> queue;
		private Map<Character, Float[]> latest;
		
		private static Map<Character, Integer> sampleSizes = new HashMap<Character, Integer>();
		
		static {
			sampleSizes.put('A', 3);
			sampleSizes.put('R', 3);
			sampleSizes.put('N', 3);
			sampleSizes.put('L', 1);
			sampleSizes.put('A', 3);
			sampleSizes.put('M', 3);
			sampleSizes.put('P', 1);
			sampleSizes.put('C', 3);
			sampleSizes.put('S', 1);
			sampleSizes.put('D', 3);
			sampleSizes.put('T', 3);
			sampleSizes.put('G', 3);
			sampleSizes.put('E', 1);
			sampleSizes.put('H', 1);
			sampleSizes.put('U', 1);
			sampleSizes.put('W', 2);
			sampleSizes.put('O', 6);
		}
		
		BTSenseMode(char sensorType, Queue<Float[]> queue, Map<Character, Float[]> latest) {
			this.sensorType = sensorType;
			sampleSize = sampleSizes.get(sensorType);
			this.queue = queue;
			this.latest = latest;
		}

		@Override
		public int sampleSize() {
			return sampleSize;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			Float[] data = (queue.isEmpty() ? latest.get(sensorType) : queue.remove());
			for(int i=0;i<sampleSize;i++) {
				sample[offset+i] = (data == null ? Float.NaN : data[i]);
			}
		}

		@Override
		public String getName() {
			return new String(new char[] {sensorType});
		}	
	}
}
