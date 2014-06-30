package lejos.robotics;

/**
 * This interface defines a listener that is notified of the tachometer when the motor starts and stops rotating.
 * It doesn't matter if start/stop rotation is caused by Motor.forward() or Motor.rotateTo(), or if the rotation
 * is terminated by rotationTo() finishing naturally or by stop().  If the motor state changes directly from forward()
 * to backward(), then a stop() is called in between which results in a new rotationEnded() and rotationStarted() notification.
 */
public interface RegulatedMotorListener {
	/**
	 * Called when the motor starts rotating.
	 */
	public void rotationStarted(RegulatedMotor motor,int tachoCount, boolean stalled, long timeStamp);
	
	/**
	 * Called when the motor stops rotating. This includes both Motor.stop() which locks the shaft, and
	 * Motor.flt() in which the shaft floats freely after power is cut to the motor. Beware: In the second case, it's possible
	 * the tachomoter reading will continue changing after notification.
	 */
	public void rotationStopped(RegulatedMotor motor,int tachoCount, boolean stalled,long timeStamp);
}
