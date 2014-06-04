package lejos.robotics.filter;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lejos.robotics.SampleProvider;

public class SubscribedProvider implements SampleProvider {
	protected DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
	private int sampleSize;
	private float[] latest;
	private PublishedSource source;
	private DataInputStream dis;
	private long timeStamp;
	private String host;
	private String name;
	private boolean active;

	public SubscribedProvider(DataInputStream dis, PublishedSource source) {
		this.dis = dis;
		sampleSize = source.sampleSize();
		latest = new float[sampleSize];
		this.source = source;
		active = true;
	}

	@Override
	public int sampleSize() {
		return sampleSize;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		try {
			// Timestamp
			timeStamp = dis.readLong();
			// Host name
			host = dis.readUTF();
			// Sample name
			name =dis.readUTF();
			// Sample size
			sampleSize = dis.readInt();
			// Sample
			for(int i=0;i<sampleSize;i++) latest[i] = dis.readFloat();
		} catch (EOFException e) {
			active = false;
			return;
		}	catch (Exception e) {
			e.printStackTrace();
			active = false;
			return;
		}

		for(int i=0;i<sampleSize;i++) sample[offset+i] = latest[i];	
	}
	
	public PublishedSource getSource() {
		return source;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getName() {
		return name;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public String getTime() {
		Date date = new Date(timeStamp);
		return formatter.format(date);
	}
}
