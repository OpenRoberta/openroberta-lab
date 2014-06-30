package lejos.remote.ev3;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.internal.ev3.EV3Key;
import lejos.remote.ev3.RMIAnalogPort;
import lejos.remote.ev3.RMIBattery;
import lejos.remote.ev3.RMIEV3;
import lejos.remote.ev3.RMII2CPort;
import lejos.remote.ev3.RMIMotorPort;
import lejos.remote.ev3.RMIUARTPort;


public class RMIRemoteEV3 extends UnicastRemoteObject implements RMIEV3 {

	private static final long serialVersionUID = -6637513883001761328L;

	public RMIRemoteEV3() throws RemoteException {
		super(0);
	}

	@Override
	public RMIBattery getBattery() throws RemoteException {
		return new RMIRemoteBattery();
	}

	@Override
	public RMIAnalogPort openAnalogPort(String portName) throws RemoteException {
		return new RMIRemoteAnalogPort(portName);
	}

	@Override
	public RMII2CPort openI2CPort(String portName) throws RemoteException {
		return new RMIRemoteI2CPort(portName);
	}

	@Override
	public RMIUARTPort openUARTPort(String portName) throws RemoteException {
		return new RMIRemoteUARTPort(portName);
	}

	@Override
	public RMIMotorPort openMotorPort(String portName) throws RemoteException {
		return new RMIRemoteMotorPort(portName);
	}

	@Override
	public RMISampleProvider createSampleProvider(String portName,
			String sensorName, String modeName) throws RemoteException {
		return new RMIRemoteSampleProvider(portName, sensorName, modeName);
	}

	@Override
	public RMIRegulatedMotor createRegulatedMotor(String portName, char motorType)
			throws RemoteException {
		return new RMIRemoteRegulatedMotor(portName, motorType);
	}

	@Override
	public RMIWifi getWifi() throws RemoteException {
		return new RMIRemoteWifi();
	}

	@Override
	public RMIBluetooth getBluetooth() throws RemoteException {
		return new RMIRemoteBluetooth();
	}

	@Override
	public RMITextLCD getTextLCD() throws RemoteException {
		return new RMIRemoteTextLCD();
	}

	@Override
	public RMIAudio getAudio() throws RemoteException {
		return new RMIRemoteAudio();
	}

	@Override
	public RMIGraphicsLCD getGraphicsLCD() throws RemoteException {
		return new RMIRemoteGraphicsLCD();
	}

	@Override
	public RMITextLCD getTextLCD(Font f) throws RemoteException {
		RMIRemoteTextLCD lcd = new RMIRemoteTextLCD();
		lcd.setFont(f);
		return lcd;
	}

	@Override
	public String getName() throws RemoteException {
		return LocalEV3.get().getName();
	}

	@Override
	public RMIKey getKey(String name) throws RemoteException {
		RMIRemoteKey key = new RMIRemoteKey();
		key.setId(name);
		return key;
	}

	@Override
	public RMILED getLED() throws RemoteException {
		return new RMIRemoteLED();
	}

	@Override
	public RMIKeys getKeys() throws RemoteException {
		return new RMIRemoteKeys();
	}
}
