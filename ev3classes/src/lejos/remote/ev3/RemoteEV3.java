package lejos.remote.ev3;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.Keys;
import lejos.hardware.LED;
import lejos.hardware.Power;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.LocalWifiDevice;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.PortException;

public class RemoteEV3 implements EV3 {
	private String host;
	private  RMIEV3 rmiEV3;
	private ArrayList<RemotePort> ports = new ArrayList<RemotePort>();
	private RemoteKeys keys;
	
	public RemoteEV3(String host) throws RemoteException, MalformedURLException, NotBoundException {
		this.host = host;
		rmiEV3 = (RMIEV3) Naming.lookup("//" + host + "/RemoteEV3");
		createPorts();
		keys = new RemoteKeys(rmiEV3.getKeys());
	}
	
	private void createPorts() {
        // Create the port objects
        ports.add(new RemotePort("S1", RemotePort.SENSOR_PORT, 0, rmiEV3));
        ports.add(new RemotePort("S2", RemotePort.SENSOR_PORT, 1, rmiEV3));
        ports.add(new RemotePort("S3", RemotePort.SENSOR_PORT, 2, rmiEV3));
        ports.add(new RemotePort("S4", RemotePort.SENSOR_PORT, 3, rmiEV3));
        ports.add(new RemotePort("A", RemotePort.MOTOR_PORT, 0, rmiEV3));
        ports.add(new RemotePort("B", RemotePort.MOTOR_PORT, 1, rmiEV3));
        ports.add(new RemotePort("C", RemotePort.MOTOR_PORT, 2, rmiEV3));
        ports.add(new RemotePort("D", RemotePort.MOTOR_PORT, 3, rmiEV3));
	}
	
	@Override
	public Port getPort(String portName) {
        for(RemotePort p : ports)
            if (p.getName().equals(portName))
                return p;
        throw new IllegalArgumentException("No such port " + portName);
	}

	@Override
	public Power getPower() {
		try {
			return new RemoteBattery(rmiEV3.getBattery());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getHost() {
		return host;
	}
	
	public RMISampleProvider createSampleProvider(String portName, String sensorName, String modeName) {
		try {
			return rmiEV3.createSampleProvider(portName, sensorName, modeName);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	public RMIRegulatedMotor createRegulatedMotor(String portName, char motorType) {
		try {
			return rmiEV3.createRegulatedMotor(portName, motorType);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	public Audio getAudio() {
		try {
			return new RemoteAudio(rmiEV3.getAudio());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	public TextLCD getTextLCD() {
		try {
			return new RemoteTextLCD(rmiEV3.getTextLCD());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	public RMIWifi getWifi() {
		try {
			return rmiEV3.getWifi();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	public RMIBluetooth getBluetooth() {
		try {
			return rmiEV3.getBluetooth();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public GraphicsLCD getGraphicsLCD() {
		try {
			return new RemoteGraphicsLCD(rmiEV3.getGraphicsLCD());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public TextLCD getTextLCD(Font f) {
		try {
			return new RemoteTextLCD(rmiEV3.getTextLCD(f));
		} catch (RemoteException e) {
			throw new PortException(e);
		}
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
		try {
			return rmiEV3.getName();
		} catch (RemoteException e) {
			throw new PortException(e);
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
	public Key getKey(String name) {
		try {
			return new RemoteKey(rmiEV3.getKey(name), keys, name);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public LED getLED() {
		try {
			return new RemoteLED(rmiEV3.getLED());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public Keys getKeys() {
		return keys;
	}
}
