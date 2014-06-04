package lejos.remote.ev3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.hardware.port.UARTPort;

public class RemoteRequestUARTPort extends RemoteRequestIOPort implements UARTPort  {
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private int portNum;
	
	public RemoteRequestUARTPort(ObjectInputStream is, ObjectOutputStream os) {
		this.is = is;
		this.os = os;
	}
	
	@Override
	public boolean open(int typ, int portNum,
			RemoteRequestPort remoteRequestPort) {
		boolean res = super.open(typ,portNum,remoteRequestPort);
		this.portNum = portNum;
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.OPEN_UART_PORT;;
		req.intValue2 = typ;
		sendRequest(req, true);
		return res;
	}
	
	@Override
	public void close() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.CLOSE_SENSOR_PORT;
		sendRequest(req, false);	
	}


	@Override
	public byte getByte() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_GET_BYTE;
		return (byte) sendRequest(req, true).reply;
	}

	@Override
	public void getBytes(byte[] vals, int offset, int len) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_GET_BYTES;
		req.intValue2 = len;
		EV3Reply reply = sendRequest(req,true);
		for(int i=0;i<len;i++) vals[offset+i] = reply.contents[i];

	}

	@Override
	public int getShort() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_GET_SHORT;
		return (byte) sendRequest(req, true).reply;
	}

	@Override
	public void getShorts(short[] vals, int offset, int len) {
		System.out.println("Getting shorts");
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_GET_SHORTS;
		req.intValue2 = len;
		EV3Reply reply = sendRequest(req,true);
		for(int i=0;i<len;i++) vals[offset+i] = reply.shorts[i];	
	}

	@Override
	public String getModeName(int mode) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_GET_MODE_NAME;
		return sendRequest(req, true).name;
	}

	@Override
	public boolean initialiseSensor(int mode) {
		System.out.println("Initialise sensor");
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_GET_BYTES;
		req.intValue2 = mode;
		return sendRequest(req, true).result;
	}

	@Override
	public void resetSensor() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_RESET_SENSOR;
		sendRequest(req, false);
	}
	
	@Override
	public boolean setMode(int mode) {
		System.out.println("Setting mode to " + mode);
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.UART_SET_MODE;
		req.intValue2 = mode;
		return sendRequest(req, true).result;
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
