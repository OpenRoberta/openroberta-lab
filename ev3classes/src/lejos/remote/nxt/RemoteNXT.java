package lejos.remote.nxt;

import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.Keys;
import lejos.hardware.LED;
import lejos.hardware.Power;
import lejos.hardware.LocalBTDevice;
import lejos.hardware.LocalWifiDevice;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.PortException;
import lejos.internal.ev3.EV3Port;

public class RemoteNXT implements NXT {
	
	private NXTCommand nxtCommand;
	private NXTComm nxtComm;
	private Power battery;
	private String name;
	private Audio audio;
	
    protected ArrayList<RemoteNXTPort> ports = new ArrayList<RemoteNXTPort>();
    
    private void createPorts()
    {
        // Create the port objects
        ports.add(new RemoteNXTPort("S1", RemoteNXTPort.SENSOR_PORT, 0, nxtCommand));
        ports.add(new RemoteNXTPort("S2", RemoteNXTPort.SENSOR_PORT, 1, nxtCommand));
        ports.add(new RemoteNXTPort("S3", EV3Port.SENSOR_PORT, 2, nxtCommand));
        ports.add(new RemoteNXTPort("S4", RemoteNXTPort.SENSOR_PORT, 3, nxtCommand));
        ports.add(new RemoteNXTPort("A", RemoteNXTPort.MOTOR_PORT, 0, nxtCommand));
        ports.add(new RemoteNXTPort("B", RemoteNXTPort.MOTOR_PORT, 1, nxtCommand));
        ports.add(new RemoteNXTPort("C", RemoteNXTPort.MOTOR_PORT, 2, nxtCommand));
    }
	
	public RemoteNXT(String name, NXTCommConnector connector) throws IOException {		
		this.name = name;
		connect(connector);
		createPorts();
	}
	
	public RemoteNXT(String name, byte[] address) {
		createPorts();
		this.name = name;
	}
	
	public void connect(NXTCommConnector connector) throws IOException {
        nxtComm = new NXTComm(connector);
        System.out.println("Connecting to " + name);
		boolean open = nxtComm.open(name, NXTConnection.LCP);
		if (!open) throw new IOException("Failed to connect to " + name);
		System.out.println("Connected");;
		nxtCommand = new NXTCommand(nxtComm);
		System.out.println("Creating remote battery");
		battery = new RemoteNXTBattery(nxtCommand);
		audio = new RemoteNXTAudio(nxtCommand);
	}
	
	public static NXT get(String name, NXTCommConnector connector) throws IOException {
		return new RemoteNXT(name, connector);
	}

	@Override
	public Port getPort(String portName) {
        for(RemoteNXTPort p : ports)
            if (p.getName().equals(portName))
                return p;
        throw new IllegalArgumentException("No such port " + portName);
	}

	@Override
	public Power getPower() {
		return battery;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public TextLCD getTextLCD() {
		throw new UnsupportedOperationException("Remote LCD not supported on the NXT");
	}

	@Override
	public TextLCD getTextLCD(Font f) {
		throw new UnsupportedOperationException("Remote LCD not supported on the NXT");
	}

	@Override
	public GraphicsLCD getGraphicsLCD() {
		throw new UnsupportedOperationException("Remote LCD not supported on the NXT");
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public String getType() {
		return "NXT";
	}

	@Override
	public String getName() {
		try {
			return nxtCommand.getFriendlyName();
		} catch (IOException e) {
			throw new PortException(e);
		}
	}

	@Override
	public LocalBTDevice getBluetoothDevice() {
		throw new UnsupportedOperationException("localBluetoothDevice not supported on the NXT");
	}

	@Override
	public LocalWifiDevice getWifiDevice() {
		throw new UnsupportedOperationException("localWifiDevice not supported on the NXT");
	}

	@Override
	public void setDefault() {
		BrickFinder.setDefault(this);	
	}

	@Override
	public Key getKey(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LED getLED() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Keys getKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public NXTCommand getNXTCommand() {
		return nxtCommand;
	}
}
