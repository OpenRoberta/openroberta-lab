package lejos.remote.ev3;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.AnalogPort;
import lejos.remote.ev3.RMIAnalogPort;

public class RMIRemoteAnalogPort extends UnicastRemoteObject implements RMIAnalogPort {
	private AnalogPort port;
	
	private static final long serialVersionUID = 3049365457299818710L;

	protected RMIRemoteAnalogPort(String portName) throws RemoteException {
		super(0);
		port = LocalEV3.get().getPort(portName).open(AnalogPort.class);
	}

	@Override
	public float getPin6() throws RemoteException {
		return port.getPin6();
	}

	@Override
	public float getPin1() throws RemoteException {
		return port.getPin1();
	}

	@Override
	public void close() throws RemoteException {
		port.close();	
	}

	@Override
	public void setPinMode(int mode) throws RemoteException {
		port.setPinMode(mode);
	}

    @Override
    public void getFloats(float[] vals, int offset, int length)
            throws RemoteException
    {
        port.getFloats(vals, offset, length);        
    }
}
