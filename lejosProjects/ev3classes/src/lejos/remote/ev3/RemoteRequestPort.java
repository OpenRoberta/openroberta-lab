package lejos.remote.ev3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lejos.hardware.DeviceException;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.IOPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3SensorConstants;

public class RemoteRequestPort implements Port {
    public static final int SENSOR_PORT = 0;
    public static final int MOTOR_PORT = 1;
    protected String name;
    protected int typ;
    protected int portNum;
    protected ObjectInputStream is;
    protected ObjectOutputStream os;
    
    public RemoteRequestPort(String name, int typ, int portNum, ObjectInputStream is, ObjectOutputStream os) {
    	this.name = name;
    	this.typ = typ;
    	this.portNum = portNum;
    	this.is = is;
    	this.os = os;
    }

	@Override
	public String getName() {
		return name;
	}

	@Override
	public <T extends IOPort> T open(Class<T> portclass) {
        RemoteRequestIOPort p = null;
        switch(typ)
        {
        case SENSOR_PORT:
            if (portclass == RemoteRequestUARTPort.class || portclass == UARTPort.class)
                p = new RemoteRequestUARTPort(is,os);
            if (portclass == RemoteRequestAnalogPort.class || portclass == AnalogPort.class)
                p = new RemoteRequestAnalogPort(is, os);
            else if (portclass == RemoteRequestI2CPort.class|| portclass == I2CPort.class)
                p = new RemoteRequestI2CPort(is, os);
            break;
        case MOTOR_PORT:
            if (portclass == BasicMotorPort.class)
                p = new RemoteRequestMotorPort(is, os);
            else if (portclass == TachoMotorPort.class)
                p = new RemoteRequestMotorPort(is, os);
            // TODO: Should we also allow Encoder?
            break;
        }
        if (p == null)
            throw new IllegalArgumentException("Invalid port interface");
        if (!p.open(typ,  portNum, this))
            throw new DeviceException("unable to open port");
        return portclass.cast(p);
	}

	@Override
	public int getPortType() {
        // TODO Should this method work for a remote port?
        return EV3SensorConstants.CONN_NONE;
	}

	@Override
	public int getSensorType() {
        // TODO Should this method work for a remote port?
        return EV3SensorConstants.TYPE_NONE;
	}
    
}
