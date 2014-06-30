package lejos.remote.ev3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.hardware.Power;

public class RemoteRequestBattery implements Power {
	private ObjectInputStream is;
	private ObjectOutputStream os;
	
	public RemoteRequestBattery(ObjectInputStream is, ObjectOutputStream os) {
		this.is = is;
		this.os = os;
	}

	@Override
	public int getVoltageMilliVolt() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.GET_VOLTAGE_MILLIVOLTS;
		return sendRequest(req, true).reply;
	}

	@Override
	public float getVoltage() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.GET_VOLTAGE;
		req.replyRequired = true;
		return sendRequest(req, true).floatReply;
	}

	@Override
	public float getBatteryCurrent() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.GET_BATTERY_CURRENT;
		return sendRequest(req, true).floatReply;
	}

	@Override
	public float getMotorCurrent() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.GET_MOTOR_CURRENT;
		return sendRequest(req, true).floatReply;
	}
	
	private EV3Reply sendRequest(EV3Request req, boolean replyRequired) {
		EV3Reply reply = null;
		req.replyRequired = replyRequired;
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
