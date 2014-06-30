package lejos.remote.nxt;

import lejos.hardware.DeviceException;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.IOPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.EV3SensorConstants;

public class RemoteNXTPort implements Port
{
    public static final int SENSOR_PORT = 0;
    public static final int MOTOR_PORT = 1;
    protected String name;
    protected int typ;
    protected int portNum;
    protected NXTCommand nxtCommand;

    public RemoteNXTPort(String name, int typ, int portNum, NXTCommand nxtCommand )
    {
        if (typ < SENSOR_PORT || typ > MOTOR_PORT)
            throw new IllegalArgumentException("Invalid port type");
        if (portNum < 0 || 
            (typ == SENSOR_PORT && portNum >= EV3SensorConstants.PORTS) ||
            (typ == MOTOR_PORT && portNum >= EV3SensorConstants.MOTORS))
            throw new IllegalArgumentException("Invalid port number");
        this.name = name;
        this.typ = typ;
        this.portNum = portNum;
        this.nxtCommand = nxtCommand;
    }
    
    /** {@inheritDoc}
     */    
    @Override
    public String getName()
    {
        return name;
    }

    /** {@inheritDoc}
     */    
    @Override
    public <T extends IOPort> T open(Class<T> portclass)
    {
        RemoteNXTIOPort p = null;
        switch(typ)
        {
        case SENSOR_PORT:
            if (portclass == RemoteNXTAnalogPort.class || portclass == AnalogPort.class)
                p = new RemoteNXTAnalogPort(nxtCommand);
            else if (portclass == RemoteNXTI2CPort.class|| portclass == I2CPort.class)
                p = new RemoteNXTI2CPort(nxtCommand);
            break;
        case MOTOR_PORT:
            if (portclass == BasicMotorPort.class)
                p = new RemoteNXTMotorPort(nxtCommand);
            else if (portclass == TachoMotorPort.class)
                p = new RemoteNXTMotorPort(nxtCommand);
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
    public int getPortType()
    {
        // TODO Should this method work for a remote port?
        return EV3SensorConstants.CONN_NONE;
    }

    @Override
    public int getSensorType()
    {
        // TODO Should this method work for a remote port?
        return EV3SensorConstants.TYPE_NONE;
    }
}
