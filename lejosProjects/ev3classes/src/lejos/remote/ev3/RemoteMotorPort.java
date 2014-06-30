package lejos.remote.ev3;

import java.rmi.RemoteException;

import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.PortException;
import lejos.hardware.port.TachoMotorPort;

public class RemoteMotorPort extends RemoteIOPort implements TachoMotorPort {
	protected RMIMotorPort rmi;
	protected RMIEV3 rmiEV3;
	
	public RemoteMotorPort(RMIEV3 rmiEV3) {
		this.rmiEV3 = rmiEV3;
	}
	
	public boolean open(int typ, int portNum, RemotePort remotePort) {
        boolean res = super.open(typ,portNum,remotePort);
		try {
			rmi = rmiEV3.openMotorPort(getName());
		} catch (RemoteException e) {
			throw new PortException(e);
		}
		return res;
	}
    /**
     * Low-level method to control a motor. 
     * 
     * @param power power from 0-100
     * @param mode defined in <code>BasicMotorPort</code>. 1=forward, 2=backward, 3=stop, 4=float.
     * @see BasicMotorPort#FORWARD
     * @see BasicMotorPort#BACKWARD
     * @see BasicMotorPort#FLOAT
     * @see BasicMotorPort#STOP
     */
    public void controlMotor(int power, int mode)
    {
    	try {
			rmi.controlMotor(power, mode);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
    }


    /**
     * returns tachometer count
     */
    public  int getTachoCount()
    {
    	try {
			return rmi.getTachoCount();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
    }
    
    /**
     *resets the tachometer count to 0;
     */ 
    public void resetTachoCount()
    {
    	try {
			rmi.resetTachoCount();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
    }
    
    public void setPWMMode(int mode)
    {
    	try {
			rmi.setPWMMode(mode);
		} catch (RemoteException e) {
			throw new PortException(e);
		}
    }
    
    public void close() {
    	try {
			rmi.close();
		} catch (RemoteException e) {
			throw new PortException(e);
		}
    }

    @Override
    public MotorRegulator getRegulator()
    {
        // TODO Does it make sense to allow this to be remote?
        throw(new UnsupportedOperationException("Remote regulators are not supported"));
        //return null;
    }
}
