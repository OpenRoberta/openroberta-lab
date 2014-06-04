package lejos.remote.ev3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.robotics.SampleProvider;

public class RemoteRequestSampleProvider implements SampleProvider {
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private int portNum;

	public RemoteRequestSampleProvider(ObjectInputStream is,
			ObjectOutputStream os, String portName, String sensorName, String modeName) {
		this.is = is;
		this.os = os;
		portNum = portName.charAt(1) - '1';
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CREATE_SAMPLE_PROVIDER;
		req.str = sensorName;
		req.str2 = portName;
		req.str3 = modeName;
		sendRequest(req, true);
	}
	
	public RemoteRequestSampleProvider(ObjectInputStream is,
			ObjectOutputStream os, String portName, String sensorName, String modeName, String topic, float frequency) {
		this.is = is;
		this.os = os;
		portNum = portName.charAt(1) - '1';
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CREATE_SAMPLE_PROVIDER_PUBLISH;
		req.str = sensorName;
		req.str2 = portName;
		req.str3 = modeName;
		req.str4 = topic;
		req.floatValue = frequency;
		sendRequest(req, true);
	}
	
	

	@Override
	public int sampleSize() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.SAMPLE_SIZE;
		return sendRequest(req, true).reply;
	}

	@Override
	public void fetchSample(float[] sample, int offset) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.FETCH_SAMPLE;
		req.replyRequired = true;
		EV3Reply reply = sendRequest(req, true);
		for(int i=0;i<reply.floats.length;i++) sample[offset+i] = reply.floats[i];
	}
	
	public void close() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CLOSE_SENSOR;
		sendRequest(req, false);
	}
	
	private EV3Reply sendRequest(EV3Request req, boolean replyRequired) {
		EV3Reply reply = null;
		req.replyRequired = replyRequired;
		req.intValue = portNum;
		try {
			os.reset();
			os.writeObject(req);
			if (replyRequired) {
				reply = (EV3Reply) is.readObject();
				if (reply.e != null) throw new RemoteRequestException(reply.e);
			}
			return reply;
		} catch (Exception e) {
			throw new RemoteRequestException(e);
		}
	}
}
