package lejos.remote.ev3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.internal.ev3.EV3Key;

public class RemoteRequestKey implements Key {
	private RemoteRequestKeys keys;
	private int iCode;
	private String name;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	
	private ArrayList<KeyListener> listeners;
	
	public RemoteRequestKey(ObjectInputStream is, ObjectOutputStream os, RemoteRequestKeys keys, String name) {
		this.is = is;
		this.os = os;
		this.keys = keys;
		this.name = name;
		this.iCode = EV3Key.getKeyId(name);
	}

	@Override
	public int getId() {
		return iCode;
	}

	@Override
	public boolean isDown() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.KEY_IS_DOWN;
		req.str = name;
		return sendRequest(req, true).result;
	}

	@Override
	public boolean isUp() {
		return !isDown();
	}

	@Override
	public void waitForPress() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.KEY_WAIT_FOR_PRESS;
		req.str = name;
		sendRequest(req, true);
	}

	@Override
	public void waitForPressAndRelease() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.KEY_WAIT_FOR_PRESS_AND_RELEASE;
		req.str = name;
		sendRequest(req, true);
	}

	@Override
	public void addKeyListener(KeyListener listener) {
	    if (listeners == null) {
	    	listeners = new ArrayList<KeyListener>();
		}
		listeners.add(listener);
		keys.addListener(iCode, this);
	}

	@Override
	public void simulateEvent(int event) {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.KEY_SIMULATE_EVENT;
		req.str = name;
		req.intValue = event;
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

	@Override
	public String getName() {
		return name;
	}
	
	public void callListeners() {
	    boolean pressed = isDown();
	    
	    if (listeners != null)
		    for(KeyListener listener: listeners)  {
		    	if (pressed) listener.keyPressed(this);
		    	else listener.keyReleased(this);
		    }
	}
}
