package lejos.remote.ev3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.MindsensorsGlideWheelMRegulatedMotor;
import lejos.hardware.port.Port;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;

public class RMIRemoteRegulatedMotor extends UnicastRemoteObject implements RMIRegulatedMotor {
	private static final long serialVersionUID = 224060987071610845L;
	private RegulatedMotor motor;
	
	protected RMIRemoteRegulatedMotor(String portName, char motorType) throws RemoteException {
		super(0);
		Port p = LocalEV3.get().getPort(portName);
		switch (motorType) {
		case 'N':
			motor = new NXTRegulatedMotor(p);
			break;
		case 'L':
			motor = new EV3LargeRegulatedMotor(p);
			break;
		case 'M':
			motor = new EV3MediumRegulatedMotor(p);
			break;
		case 'G':
			motor = new MindsensorsGlideWheelMRegulatedMotor(p);
		}		
	}

	@Override
	public void addListener(RegulatedMotorListener listener)
			throws RemoteException {
		motor.addListener(listener);
	}

	@Override
	public RegulatedMotorListener removeListener() throws RemoteException {
		return motor.removeListener();
	}

	@Override
	public void stop(boolean immediateReturn) throws RemoteException {
		motor.stop(immediateReturn);
	}

	@Override
	public void flt(boolean immediateReturn) throws RemoteException {
		motor.flt(immediateReturn);
	}

	@Override
	public void waitComplete() throws RemoteException {
		motor.waitComplete();	
	}

	@Override
	public void rotate(int angle, boolean immediateReturn)
			throws RemoteException {
		motor.rotate(angle, immediateReturn);
	}

	@Override
	public void rotate(int angle) throws RemoteException {
		motor.rotate(angle);
	}

	@Override
	public void rotateTo(int limitAngle) throws RemoteException {
		motor.rotateTo(limitAngle);	
	}

	@Override
	public void rotateTo(int limitAngle, boolean immediateReturn)
			throws RemoteException {
		motor.rotateTo(limitAngle, immediateReturn);
	}

	@Override
	public int getLimitAngle() throws RemoteException {
		return motor.getLimitAngle();
	}

	@Override
	public void setSpeed(int speed) throws RemoteException {
		motor.setSpeed(speed);
	}

	@Override
	public int getSpeed() throws RemoteException {
		return motor.getSpeed();
	}

	@Override
	public float getMaxSpeed() throws RemoteException {
		return motor.getMaxSpeed();
	}

	@Override
	public boolean isStalled() throws RemoteException {
		return motor.isStalled();
	}

	@Override
	public void setStallThreshold(int error, int time) throws RemoteException {
		motor.setStallThreshold(error, time);	
	}

	@Override
	public void setAcceleration(int acceleration) throws RemoteException {
		motor.setAcceleration(acceleration);
	}

	@Override
	public void close() throws RemoteException {
		motor.close();
	}

	@Override
	public void forward() throws RemoteException {
		motor.forward();	
	}

	@Override
	public void backward() throws RemoteException {
		motor.backward();	
	}

	@Override
	public void resetTachoCount() throws RemoteException {
		motor.resetTachoCount();
	}

	@Override
	public int getTachoCount() throws RemoteException {
		return motor.getTachoCount();
	}
}

