package lejos.remote.ev3;

import lejos.hardware.port.BasicSensorPort;
import lejos.hardware.port.IOPort;
import lejos.hardware.sensor.EV3SensorConstants;
import lejos.remote.nxt.RemoteNXTPort;

public class RemoteIOPort implements IOPort, BasicSensorPort, EV3SensorConstants {
    protected int port = -1;
    protected int typ = -1;
    protected RemotePort ref;
    protected int currentMode = 0;
	protected static RemoteIOPort [][] openPorts = new RemoteIOPort[RemoteNXTPort.MOTOR_PORT+1][PORTS];

	public boolean open(int typ, int port, RemotePort ref) {
        synchronized (openPorts)
        {
            if (openPorts[typ][port] == null)
            {
                openPorts[typ][port] = this;
                this.port = port;
                this.typ = typ;
                this.ref = ref;
                return true;
            }
            return false;
        }
	}

	@Override
	public int getMode() {
        return currentMode;
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public boolean setMode(int mode) {
		currentMode = mode;
		return false;
	}

	@Override
	public boolean setType(int type) {
		throw new UnsupportedOperationException("This operation is for legacy modes only");
	}

	@Override
	public boolean setTypeAndMode(int type, int mode) {
        setType(type);
        setMode(mode);
        return true;
	}

	@Override
	public void close() {
        if (port == -1)
            throw new IllegalStateException("Port is not open");
        synchronized (openPorts)
        {
            openPorts[typ][port] = null;
            port = -1;
        }
	}

	@Override
	public String getName() {
		return ref.getName();
	}

	@Override
	public void setPinMode(int mode) {
		// Overridden by specific port implementations
	}
}
