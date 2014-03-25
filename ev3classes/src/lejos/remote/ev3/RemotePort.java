package lejos.remote.ev3;

import lejos.hardware.DeviceException;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.IOPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3SensorConstants;

public class RemotePort implements Port
{
    public static final int SENSOR_PORT = 0;
    public static final int MOTOR_PORT = 1;
    protected String name;
    protected int typ;
    protected int portNum;
    protected RMIEV3 rmiEV3;

    public RemotePort(String name, int typ, int portNum, RMIEV3 rmiEV3)
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
        this.rmiEV3 = rmiEV3;
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
        RemoteIOPort p = null;
        switch(typ)
        {
        case SENSOR_PORT:
            if (portclass == UARTPort.class)
                p = new RemoteUARTPort(rmiEV3);
            if (portclass == RemoteAnalogPort.class || portclass == AnalogPort.class)
                p = new RemoteAnalogPort(rmiEV3);
            else if (portclass == RemoteI2CPort.class|| portclass == I2CPort.class)
                p = new RemoteI2CPort(rmiEV3);
            break;
        case MOTOR_PORT:
            if (portclass == BasicMotorPort.class)
                p = new RemoteMotorPort(rmiEV3);
            else if (portclass == TachoMotorPort.class)
                p = new RemoteMotorPort(rmiEV3);
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
