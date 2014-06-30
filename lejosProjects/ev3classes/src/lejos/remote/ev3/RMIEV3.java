package lejos.remote.ev3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import lejos.hardware.lcd.Font;

public interface RMIEV3 extends Remote {

	public RMIAnalogPort openAnalogPort(String portName) throws RemoteException;
	
	public RMII2CPort openI2CPort(String portName) throws RemoteException;

	public RMIBattery getBattery() throws RemoteException;

	public RMIUARTPort openUARTPort(String portName) throws RemoteException;

	public RMIMotorPort openMotorPort(String portName) throws RemoteException;
	
	public RMISampleProvider createSampleProvider(String portName, String sensorName, String modeName) throws RemoteException;
	
	public RMIRegulatedMotor createRegulatedMotor(String portName, char motorType) throws RemoteException;

	public RMIAudio getAudio() throws RemoteException;
	
	public RMITextLCD getTextLCD() throws RemoteException;
	
	public RMITextLCD getTextLCD(Font f) throws RemoteException;
	
	public RMIGraphicsLCD getGraphicsLCD() throws RemoteException;
	
	public RMIWifi getWifi() throws RemoteException;
	
	public RMIBluetooth getBluetooth() throws RemoteException;
	
	public String getName() throws RemoteException;
	
	public RMIKey getKey(String name) throws RemoteException;

	public RMILED getLED() throws RemoteException;

	public RMIKeys getKeys()  throws RemoteException;
	
}
