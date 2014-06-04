package lejos.robotics.filter;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lejos.utility.Delay;

public class PublishedSource {
	protected static final int MAX_PACKET_SIZE = 64;
	protected static final int PUBLISH_PORT = 3017;
	protected static final int SOCKET_TIMEOUT = 3000;
	protected static final int MAX_AGE = 10000;
	protected static final int MAX_SAMPLE_MESSAGE_SIZE = 128;
	
	private static Listener listener = new Listener();
	
	static {
		listener.setDaemon(true);
		listener.start();
	}
	
	protected String name;
	protected int sampleSize;
	protected float[] sample;
	protected float frequency;
	protected String host;
	protected int port;
	protected long timeStamp;
	protected DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
	protected String ipAddress;
	protected Socket socket;
	protected DataInputStream dis;
	
	public PublishedSource(String ipAddress, byte[] message) throws IOException {
		this.ipAddress = ipAddress;
		timeStamp = System.currentTimeMillis();
		ByteArrayInputStream bis = new ByteArrayInputStream(message);
		dis = new DataInputStream(bis);
		host = dis.readUTF();
		port = dis.readInt();
		name = dis.readUTF();
		sampleSize = dis.readInt();
		frequency = dis.readFloat();
		dis.close();
		bis.close();
	}
	
	public SubscribedProvider connect() throws IOException {
		socket = new Socket(ipAddress, port);
	    DataInputStream dis = new DataInputStream(socket.getInputStream());
		return new SubscribedProvider(dis, this);
	}
	
	public int sampleSize() {
		return sampleSize;
	}
	
	public String getName() {
		return name;
	}
	
	public float getFrequency() {
		return frequency;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getTime() {
		Date date = new Date(timeStamp);
		return formatter.format(date);
	}
	
	public String getKey() {
		return host + ":" + port;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public int getPort() {
		return port;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void close() throws IOException {
		if (dis != null) dis.close();
		if (socket != null) socket.close();
	}
	
	private static Map<String,PublishedSource> sources = new HashMap<String,PublishedSource>();
	
	public static Collection<PublishedSource> getSources() {
		Delay.msDelay(SOCKET_TIMEOUT); // Wait to get new sources
		synchronized (sources) {
			return sources.values();
		}
	}
	
	private static class Listener extends Thread {
		@Override
		public void run() {
			DatagramSocket socket = null;
			DatagramPacket packet = new DatagramPacket (new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
			
			try {
				socket = new DatagramSocket(PUBLISH_PORT);
				socket.setSoTimeout(SOCKET_TIMEOUT);
				
				for(;;) {
					try {
						socket.receive (packet);
						String ip = packet.getAddress().getHostAddress();
						PublishedSource source = new PublishedSource(ip, packet.getData());
						synchronized (sources) {
							sources.put(source.getKey(), source);
						}
					} catch (SocketTimeoutException e) {
						// Ignore
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					// Remove anything too old from sources
					ArrayList<String> removeKeys = new ArrayList<String>();
					
					synchronized (sources) {
						for(String key: sources.keySet()) {
							if (System.currentTimeMillis() - sources.get(key).getTimeStamp() > MAX_AGE) {
								removeKeys.add(key);
							}
						}
						for(String key: removeKeys) sources.remove(key);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null) socket.close();
			}
		}
	}
}
