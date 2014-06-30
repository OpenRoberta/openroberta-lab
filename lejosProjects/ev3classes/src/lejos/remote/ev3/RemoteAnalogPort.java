package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.port.AnalogPort;
import lejos.hardware.port.PortException;

public class RemoteAnalogPort extends RemoteIOPort  implements AnalogPort {
	protected RMIAnalogPort rmi;
	protected RMIEV3 rmiEV3;
	
	public RemoteAnalogPort(RMIEV3 rmiEV3) {
		this.rmiEV3 = rmiEV3;
	}
	
	public boolean open(int typ, int portNum, RemotePort remotePort) {
        boolean res = super.open(typ,portNum,remotePort);
		try {
			rmi = rmiEV3.openAnalogPort(getName());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
		return res;
	}
	
	@Override
	public void close() {
		super.close();
		try {
			rmi.close();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public float getPin6() {
		try {
			return rmi.getPin6();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}

	@Override
	public float getPin1() {
		try {
			return rmi.getPin1();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
	@Override
	public void setPinMode(int mode) {
		try {
			rmi.setPinMode(mode);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
	}
	
    // The following method provide compatibility with NXT sensors
    
    @Override
    public boolean setType(int type)
    {
        switch(type)
        {
        case TYPE_NO_SENSOR:
        case TYPE_SWITCH:
        case TYPE_TEMPERATURE:
        case TYPE_CUSTOM:
        case TYPE_ANGLE:
            setPinMode(CMD_FLOAT);
            break;
        case TYPE_LIGHT_ACTIVE:
        case TYPE_SOUND_DBA:            
        case TYPE_REFLECTION:
            setPinMode(CMD_SET|CMD_PIN5);
            break;
        case TYPE_LIGHT_INACTIVE:
        case TYPE_SOUND_DB: 
            setPinMode(CMD_SET);
            break;
        case TYPE_LOWSPEED:
            setPinMode(CMD_SET);
            break;
        case TYPE_LOWSPEED_9V:
            setPinMode(CMD_SET|CMD_PIN1);
            break;
        default:
            throw new UnsupportedOperationException("Unrecognised sensor type");
        }
        return true;
    }
    

    @Override
    public void getFloats(float[] vals, int offset, int length)
    {
        try {
            rmi.getFloats(vals, offset, length);
        } catch (RemoteException e) {
			throw new PortException(e);
        }
    }
}
