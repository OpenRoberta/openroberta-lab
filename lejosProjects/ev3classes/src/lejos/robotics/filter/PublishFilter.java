package lejos.robotics.filter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import lejos.hardware.ev3.LocalEV3;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class PublishFilter extends AbstractFilter  {
	protected static final int PUBLISH_PORT = 3017;
	protected static final int UDP_PERIOD = 1000;
	
	protected String name;
	protected String host;
	protected DatagramSocket datagramSocket;
	protected Publisher publisher = new Publisher();
	protected Listener listener = new Listener();
	protected float[] latest;
	protected float frequency;
	protected byte[] publishMessage;
	protected DatagramPacket publishPacket;
	protected ServerSocket ss;
	protected ArrayList<Socket> subscribers = new ArrayList<Socket>();

	public PublishFilter(SampleProvider source, String name, float frequency) throws IOException {
		super(source);
		this.name = name;
		this.frequency = frequency;
		latest = new float[sampleSize];
		ss = new ServerSocket(0);
		host = LocalEV3.get().getName();
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(buf);
		// Host name
		dos.writeUTF(LocalEV3.get().getName());
		// Port number
		dos.writeInt(ss.getLocalPort());
		// Sample name
		dos.writeUTF(name);
		// Sample size
		dos.writeInt(sampleSize);
		// Frequency
		dos.writeFloat(frequency);
		dos.close();
		buf.close(); 
		publishMessage =  buf.toByteArray();
		publishPacket =  new DatagramPacket(publishMessage, publishMessage.length, InetAddress.getByName("255.255.255.255"), PUBLISH_PORT);
		datagramSocket = new DatagramSocket();
		datagramSocket.setBroadcast(true);
		publisher.setDaemon(true);
		publisher.start();
		listener.setDaemon(true);
		listener.start();
	}
	
	@Override
	public void fetchSample(float[] sample, int offset) {
		byte[] sampleMessage;
		source.fetchSample(latest, offset);
		for(int i=0;i<sampleSize();i++) sample[offset+i] = latest[i];
		
		// Put the sample message in a byte array
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(buf);
			// Timestamp
			dos.writeLong(System.currentTimeMillis());
			// Host name
			dos.writeUTF(host);
			// Sample name
			dos.writeUTF(name);
			// Sample size
			dos.writeInt(sampleSize);
			// Sample
			for(int i=0;i<sampleSize;i++) dos.writeFloat(sample[i]);
			dos.close();
			buf.close();
			sampleMessage = buf.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// Send the data to all listeners and remove inactive ones
		synchronized (listener) {
			ArrayList<Socket> active = new ArrayList<Socket>();
			
			for(Socket s: subscribers) {
				try {
					OutputStream os = s.getOutputStream();
					os.write(sampleMessage);
					os.flush();
				    active.add(s);
				} catch (IOException e) {
					try {
						s.close();
					} catch (IOException e1) {
						// Ignore
					}
				}
			}
			
			subscribers = active;
		}
	}
	/*
	 * Continually send UDP message to publicise this source
	 */
	private class Publisher extends Thread {
		@Override
		public void run() {
			for(;;) {    
			    try {
					datagramSocket.send(publishPacket);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			    Delay.msDelay(UDP_PERIOD);
			}
		}
	}
	
	// Listen for new subscribers to the source
	private class Listener extends Thread {
		@Override
		public void run() {
			for(;;) {
				try {
					Socket s = ss.accept();
					synchronized (listener) {
						subscribers.add(s);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
