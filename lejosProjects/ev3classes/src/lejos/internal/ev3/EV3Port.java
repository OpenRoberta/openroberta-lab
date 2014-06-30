package lejos.internal.ev3;

import lejos.hardware.DeviceException;
import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.I2CPort;
import lejos.hardware.port.IOPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3SensorConstants;

public class EV3Port implements Port
{
    public static final int SENSOR_PORT = 0;
    public static final int MOTOR_PORT = 1;
    protected String name;
    protected int typ;
    protected int portNum;
    protected static final EV3DeviceManager devMan = EV3DeviceManager.getLocalDeviceManager();

    public EV3Port(String name, int typ, int portNum)
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
        EV3IOPort p = null;
        switch(typ)
        {
        case SENSOR_PORT:
            if (portclass == UARTPort.class)
                p = new EV3UARTPort();
            else if (portclass == AnalogPort.class)
                p = new EV3AnalogPort();
            else if (portclass == I2CPort.class)
                p = new EV3I2CPort();
            break;
        case MOTOR_PORT:
            if (portclass == BasicMotorPort.class)
                p = new EV3MotorPort();
            else if (portclass == TachoMotorPort.class)
                p = new EV3MotorPort();
            // TODO: Should we also allow Encoder?
            break;
        }
        if (p == null)
            throw new IllegalArgumentException("Invalid port interface");
        if (!p.open(typ,  portNum, this))
            throw new DeviceException("unable to open port");
        return portclass.cast(p);
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getPortType()
    {
        return devMan.getPortType(portNum);
    }

    /** {@inheritDoc}
     */    
    @Override
    public int getSensorType()
    {
        return devMan.getSensorType(portNum);
    }
}
