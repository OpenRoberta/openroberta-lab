package lejos.hardware.device;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.RegulatedMotorListener;
import lejos.utility.Delay;

/**
* Abstraction to drive a regulated encoder motor with the NXTMMX motor multiplexer. 
* The 
* NXTMMX motor multiplexer device allows you to connect two 
* additional motors to your robot using a sensor port. Multiple NXTMMXs can be chained together.
* <p>
* Use the <code>NXTMMX.getRegulatedMotor()</code> factory method to retrieve an instance of this class.
* 
* @see NXTMMX
* @author Kirk P. Thompson  
*
*/
public class MMXRegulatedMotor extends MMXMotor implements RegulatedMotor {
	static final int LISTENERSTATE_STOP = 0;
    static final int LISTENERSTATE_START = 1;
    private final int MOTOR_MAX_DPS=Math.round(mmx.getVoltage()*100);
    private Object lockObj = new Object();
    
    RegulatedMotorListener listener;
    
	MMXRegulatedMotor(NXTMMX mmx, int channel) {
		super(mmx, channel);
	}
	
    
	public void addListener(@SuppressWarnings("hiding") RegulatedMotorListener listener) {
        this.listener = listener;
    }
    
    void doListenerState(int listenerState) {
        synchronized (lockObj) {
    	if (this.listener == null) return;
        if (listenerState == LISTENERSTATE_STOP) {
        	// wait for a complete stop before notifying
//        	new Thread(new Runnable(){
//				public void run() {
//					waitComplete();
//					listener.rotationStopped(MMXRegulatedMotor.this, getTachoCount(), false, System.currentTimeMillis());
//				}}).start();
        	listener.rotationStopped(MMXRegulatedMotor.this, getTachoCount(), false, System.currentTimeMillis());
        } else {
            this.listener.rotationStarted(this, getTachoCount(), false, System.currentTimeMillis());
        }
        }
    }
    
    public RegulatedMotorListener removeListener() {
        RegulatedMotorListener old = this.listener;
        this.listener = null;
        return old;
    }
    
    /**
     * Return the current rotational speed calculated from the encoder position every 100 ms. This will
     * likely differ from what was specified in <code>setSpeed</code>.
     * 
     * @return The current rotational speed in deg/sec
     */
    public int getRotationSpeed() {
        return Math.round(.01f * mmx.doCommand(NXTMMX.CMD_GETSPEED, 0, channel));
    }
    
    public void stop(boolean immediateReturn) {
        super.stop();
        if (!immediateReturn) waitComplete();
    }

    public void flt(boolean immediateReturn) {
        super.flt();
        if (!immediateReturn) waitComplete();
    }

    public synchronized void waitComplete() {
    	while (isMoving()) {
            Delay.msDelay(50);
        }
    }
    
    /**
     * Rotate by the requested number of degrees while blocking until completion.
     * 
     * @param angle number of degrees to rotate relative to the current position.
     */
    public void rotate(int angle) {
        rotate(angle, false);
    }
    
    /**
     * Rotate to the target angle while blocking until completion.
     * 
     * @param limitAngle Angle [in degrees] to rotate to.
     */
    public void rotateTo(int limitAngle) {
        rotateTo(limitAngle, false);
    }
    
    /**
     * Rotate by the requested number of degrees with option for wait until completion or immediate return where the motor
     * completes its rotation asynchronously.
     * 
     * @param degrees number of degrees to rotate relative to the current position.
     * @param immediateReturn if <code>true</code>, do not wait for the move to complete. <code>false</code> will block
     * until the rotation completes.
     */
    public void rotate(int degrees, boolean immediateReturn){
        mmx.doCommand(NXTMMX.CMD_ROTATE, degrees, channel);
        if (!immediateReturn) mmx.waitRotateComplete(channel);
    }
    
    /**
     * Rotate to the target angle with option for wait until completion or immediate return where the motor
     * completes its rotation asynchronously.
     * 
     * @param limitAngle Angle [in degrees] to rotate to.
     * @param immediateReturn if <code>true</code>, do not wait for the move to complete. <code>false</code> will block
     * until the rotation completes.
     */
    public void rotateTo(int limitAngle, boolean immediateReturn){
    	mmx.doCommand(NXTMMX.CMD_ROTATE_TO, limitAngle, channel);
        if (!immediateReturn) mmx.waitRotateComplete(channel);
    }
	
    /**
     * Sets desired motor speed, in degrees per second. 
     * <p>
     * The NXTMMX does not provide speed control per se (just power) so we approximate the power value used
     * based on the requested degress/sec (dps) passed in <code>speed</code>. This means if you request 400 dps, 
     * the actual dps value
     * may not reflect that. Setting speed during a rotate method will have no effect on the running rotate
     *  but will on the next rotate
     * method call.
     * <p> 
     * experimental data gives: dps=8.1551*power+32.253 (unloaded @ 8.83V)
     * <p>
     * <b>Note:</b>The NXTMMX doesn't seem to want to drive the standard NXT motor below ~40 dps.
     * @param speed Motor speed in degrees per second
     * @see #getSpeed
     * @see #setPower
     */
    public void setSpeed(int speed) {
    	speed=Math.abs(speed);
        if (speed > MOTOR_MAX_DPS) speed=MOTOR_MAX_DPS;
        float power=(speed-32.253f)/8.1551f;
        if (power<0) power=0;
        super.setPower(Math.round(power));
    }

    /**
     * Return the current target speed.
     * @return Motor speed in degrees per second.
     * @see #setSpeed
     * @see #getPower
     */
    public int getSpeed() {
    	return Math.round(8.1551f*super.getPower()+32.253f);
    }
    
    public float getMaxSpeed() {
        return MOTOR_MAX_DPS;
    }
    

    public boolean isStalled() {
    	return NXTMMX.MOTPARAM_OP_TRUE==mmx.doCommand(NXTMMX.CMD_ISSTALLED, 0, channel);
    }
    
    /**
     * NOT IMPLEMENTED as the NXTMMX motor controller does not support this command.
     */
    public void setStallThreshold(int error, int time) {
    	// do nothing
    }
    
    /**
     * Sets speed ramping is enabled/disabled for this motor. The <code>RegulatedMotor</code> interface 
     * specifies this in degrees/sec/sec
     * but the NXTMMX does not allow the rate to be changed, just if the motor uses smooth 
     * acceleration or not so we use the <code>acceleration</code>
     * parameter to specify ramping state. <p>Default at instantiation is ramping enabled.
     * @param acceleration >0 means NXTMMX internal ramping is enabled otherwise disabled
     * @see MMXMotor#setRamping(boolean)
     */
   public void setAcceleration(int acceleration){
       super.setRamping(acceleration>0);
   }

   /**
    * Return the angle that this Motor is rotating to or last rotated to. 
    * @return angle in degrees. 0 if no rotate method has been intiated.
    */
	public int getLimitAngle() {
		return mmx.doCommand(NXTMMX.CMD_GETLIMITANGLE, 0, channel);
	}


	@Override
	public void close() {
		// Do nothing
	}
}
