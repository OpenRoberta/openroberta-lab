package lejos.remote.ev3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.Keys;
import lejos.hardware.LED;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.LocalWifiDevice;
import lejos.hardware.Power;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.ArcRotateMoveController;

public class RemoteRequestEV3 implements EV3, Serializable {
	private static final long serialVersionUID = -7784568187751439269L;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private Socket socket;
	private ArrayList<RemoteRequestPort> ports = new ArrayList<RemoteRequestPort>();
	private RemoteRequestKeys keys;
	
	private static final int PORT = 8002;
	
	public RemoteRequestEV3(String host) throws IOException {
		socket = new Socket(host,PORT);
		is = new ObjectInputStream(socket.getInputStream());
		os = new ObjectOutputStream(socket.getOutputStream());
		createPorts();
		keys = new RemoteRequestKeys(is, os);
	}
	
	private void createPorts() {
        // Create the port objects
        ports.add(new RemoteRequestPort("S1", RemoteRequestPort.SENSOR_PORT, 0, is, os));
        ports.add(new RemoteRequestPort("S2", RemoteRequestPort.SENSOR_PORT, 1, is, os));
        ports.add(new RemoteRequestPort("S3", RemoteRequestPort.SENSOR_PORT, 2, is, os));
        ports.add(new RemoteRequestPort("S4", RemoteRequestPort.SENSOR_PORT, 3, is, os));
        ports.add(new RemoteRequestPort("A", RemoteRequestPort.MOTOR_PORT, 0, is, os));
        ports.add(new RemoteRequestPort("B", RemoteRequestPort.MOTOR_PORT, 1, is, os));
        ports.add(new RemoteRequestPort("C", RemoteRequestPort.MOTOR_PORT, 2, is, os));
        ports.add(new RemoteRequestPort("D", RemoteRequestPort.MOTOR_PORT, 3, is, os));
	}
	
	@Override
	public Port getPort(String portName) {
        for(RemoteRequestPort p : ports)
            if (p.getName().equals(portName))
                return p;
        throw new IllegalArgumentException("No such port " + portName);
	}

	@Override
	public Power getPower() {
		return new RemoteRequestBattery(is, os);
	}

	@Override
	public Audio getAudio() {
		return new RemoteRequestAudio(is, os);
	}
	
	@Override
	public TextLCD getTextLCD() {
		return new RemoteRequestTextLCD(is, os);
	}

	@Override
	public TextLCD getTextLCD(Font f) {
		return new RemoteRequestTextLCD(is, os, f);
	}

	@Override
	public GraphicsLCD getGraphicsLCD() {
		return new RemoteRequestGraphicsLCD(is, os);
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public String getType() {
		return "EV3";
	}

	@Override
	public String getName() {
		EV3Request req = new EV3Request();
		req.request = EV3Request.Request.GET_NAME;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			EV3Reply reply = (EV3Reply) is.readObject();
			return reply.name;
		} catch (Exception e) {
			return "Not knon";
		}
	}

	@Override
	public LocalBTDevice getBluetoothDevice() {
		return null;
	}

	@Override
	public LocalWifiDevice getWifiDevice() {
		return null;
	}

	@Override
	public void setDefault() {
		BrickFinder.setDefault(this);
	}

	@Override
	public Keys getKeys() {
		return keys;
	}

	@Override
	public Key getKey(String name) {
		return new RemoteRequestKey(is, os, keys, name);
	}

	@Override
	public LED getLED() {
		return new RemoteRequestLED(is,os);
	}
	
	public SampleProvider createSampleProvider(String portName, String sensorName, String modeName) {
		return new RemoteRequestSampleProvider(is,os, portName, sensorName, modeName);
	}
	
	public SampleProvider createSampleProvider(String portName,
			String sensorName, String modeName, String topic, float frequency) throws RemoteException {
		return new RemoteRequestSampleProvider(is, os, portName, sensorName, modeName, topic, frequency);
	}
	
	public RegulatedMotor createRegulatedMotor(String portName, char motorType) {
		return new RemoteRequestRegulatedMotor(is, os, portName, motorType);
	}
	
	
	public ArcRotateMoveController createPilot(double wheelDiameter, double trackWidth, String leftMotor, String rightMotor) {
		return new RemoteRequestPilot(is, os, leftMotor, rightMotor, wheelDiameter, trackWidth);
	}
	
	public void disConnect() {
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
