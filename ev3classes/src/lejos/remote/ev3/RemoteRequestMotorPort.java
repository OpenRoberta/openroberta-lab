package lejos.remote.ev3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.TachoMotorPort;

public class RemoteRequestMotorPort extends RemoteRequestIOPort implements TachoMotorPort {
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private int portNum;

	public RemoteRequestMotorPort(ObjectInputStream is, ObjectOutputStream os) {
		this.is = is;
		this.os = os;
	}
	
	@Override
	public boolean open(int typ, int portNum,
			RemoteRequestPort remoteRequestPort) {
		boolean res = super.open(typ,portNum,remoteRequestPort);
		this.portNum = portNum;
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.OPEN_MOTOR_PORT;
		req.intValue2 = typ;
		sendRequest(req, false);
		return res;
	}
	
	@Override
	public void close() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CLOSE_MOTOR_PORT;
		sendRequest(req, false);	
	}

	@Override
	public void controlMotor(int power, int mode) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CONTROL_MOTOR;
		req.intValue2 = power;
		req.intValue3 = mode;
		sendRequest(req, false);
	}

	@Override
	public void setPWMMode(int mode) {
		// Not implemented
	}

	@Override
	public int getTachoCount() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.GET_TACHO_COUNT;
		return sendRequest(req, true).reply;
	}

	@Override
	public void resetTachoCount() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.RESET_TACHO_COUNT;
		sendRequest(req, false);
	}

	@Override
	public MotorRegulator getRegulator() {
		throw(new UnsupportedOperationException("Remote regulators are not supported"));
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
