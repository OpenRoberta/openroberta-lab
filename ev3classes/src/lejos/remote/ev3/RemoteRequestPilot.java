package lejos.remote.ev3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;

public class RemoteRequestPilot implements ArcRotateMoveController {
	private ObjectInputStream is;
	private ObjectOutputStream os;

	public RemoteRequestPilot(ObjectInputStream is, ObjectOutputStream os, String leftMotor, String rightMotor, double wheelDiameter, double trackWidth) {
		this.is = is;
		this.os = os;
		
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CREATE_REGULATED_MOTOR;
		req.str = leftMotor;
		req.ch = 'L';
		sendRequest(req, false);
		
		req = new EV3Request();
		req.request = EV3Request.Request.CREATE_REGULATED_MOTOR;
		req.str = rightMotor;
		req.ch = 'L';
		sendRequest(req, false);
		
		req = new EV3Request();
		req.request = EV3Request.Request.CREATE_PILOT;
		req.doubleValue = wheelDiameter;
		req.doubleValue2 = trackWidth;
		req.str = leftMotor;
		req.str2 = rightMotor;
		sendRequest(req, true);
	}

	@Override
	public double getMinRadius() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_GET_MIN_RADIUS;
		return sendRequest(req, true).doubleReply;
	}

	@Override
	public void setMinRadius(double radius) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_SET_MIN_RADIUS;
		req.doubleValue = radius;
		sendRequest(req, false);
	}

	@Override
	public void arcForward(double radius) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_ARC_FORWARD;
		req.doubleValue = radius;
		sendRequest(req, false);
	}

	@Override
	public void arcBackward(double radius) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_ARC_BACKWARD;
		req.doubleValue = radius;
		sendRequest(req, false);
	}

	@Override
	public void arc(double radius, double angle) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_ARC;
		req.doubleValue = radius;
		req.doubleValue2 = angle;
		sendRequest(req, true);	
	}

	@Override
	public void arc(double radius, double angle, boolean immediateReturn) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_ARC_IMMEDIATE;
		req.doubleValue = radius;
		req.doubleValue2 = angle;
		req.flag = immediateReturn;
		sendRequest(req, !immediateReturn);	
	}

	@Override
	public void travelArc(double radius, double distance) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_TRAVEL_ARC;
		req.doubleValue = radius;
		req.doubleValue2 = distance;
		sendRequest(req, true);	
		
	}

	@Override
	public void travelArc(double radius, double distance,
			boolean immediateReturn) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_TRAVEL_ARC_IMMEDIATE;
		req.doubleValue = radius;
		req.doubleValue2 = distance;
		req.flag = immediateReturn;
		sendRequest(req, !immediateReturn);
	}

	@Override
	public void forward() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_FORWARD;
		sendRequest(req, false);
	}

	@Override
	public void backward() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_BACKWARD;
		sendRequest(req, false);
	}

	@Override
	public void stop() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_STOP;
		sendRequest(req, false);
	}

	@Override
	public boolean isMoving() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_IS_MOVING;
		return sendRequest(req, true).result;
	}

	@Override
	public void travel(double distance) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_TRAVEL;
		req.doubleValue = distance;
		sendRequest(req, true);
	}

	@Override
	public void travel(double distance, boolean immediateReturn) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_TRAVEL_IMMEDIATE;
		req.doubleValue = distance;
		req.flag = immediateReturn;
		sendRequest(req, !immediateReturn);
	}

	@Override
	public void setTravelSpeed(double speed) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_SET_TRAVEL_SPEED;
		req.doubleValue = speed;
		sendRequest(req, false);
	}

	@Override
	public double getTravelSpeed() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_GET_TRAVEL_SPEED;
		return sendRequest(req, true).doubleReply;
	}

	@Override
	public double getMaxTravelSpeed() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_GET_MAX_TRAVEL_SPEED;
		return sendRequest(req, true).doubleReply;
	}

	@Override
	public Move getMovement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMoveListener(MoveListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(double angle) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_ROTATE;
		req.doubleValue = angle;
		sendRequest(req, true);
	}

	@Override
	public void rotate(double angle, boolean immediateReturn) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_ROTATE_IMMEDIATE;
		req.doubleValue = angle;
		req.flag = immediateReturn;
		sendRequest(req, !immediateReturn);
	}
	
	public void steer(double turnRate) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_STEER;
		req.doubleValue = turnRate;
		sendRequest(req, false);
	}

	@Override
	public void setRotateSpeed(double speed) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_SET_ROTATE_SPEED;
		req.doubleValue = speed;
		sendRequest(req, false);
	}

	@Override
	public double getRotateSpeed() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_GET_ROTATE_SPEED;
		return sendRequest(req, true).doubleReply;
	}

	@Override
	public double getRotateMaxSpeed() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.PILOT_GET_MAX_ROTATE_SPEED;
		return sendRequest(req, true).doubleReply;
	}
	
	public void close() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CLOSE_PILOT;
		sendRequest(req, true);
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
