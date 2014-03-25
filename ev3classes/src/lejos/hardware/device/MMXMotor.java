package lejos.hardware.device;

import lejos.robotics.EncoderMotor;

/**
 * Abstraction to drive a basic encoder motor with the NXTMMX motor multiplexer. The 
 * NXTMMX motor multiplexer device allows you to connect two 
 * additional motors to your robot using a sensor port. Multiple NXTMMXs can be chained together.
 * <p>
 * Use the <code>NXTMMX.getBasicMotor()</code> factory method to retrieve an instance of this class.
 * 
 * @see NXTMMX
 * @see MMXRegulatedMotor
 * @author Kirk P. Thompson  
 *
 */
public class MMXMotor implements EncoderMotor {

	NXTMMX mmx;
    int channel;
    
    MMXMotor(NXTMMX mmx, int channel) {
        this.mmx=mmx;
        this.channel=channel;
        setPower(100);
    }

    public void setPower(int power) {
        power=Math.abs(power);
        if (power>100) power=100;
        mmx.doCommand(NXTMMX.CMD_SETPOWER, power, channel);
    }

    /**
     * Sets speed ramping is enabled/disabled for this motor. Default at instantiation is ramping enabled.
     * @param doRamping <code>true</code> to enable, <code>false </code>to disable
     */    
    public void setRamping(boolean doRamping) {
    	int operand=NXTMMX.MOTPARAM_OP_FALSE;
        if (doRamping) operand=NXTMMX.MOTPARAM_OP_TRUE;
    	mmx.doCommand(NXTMMX.CMD_SETRAMPING, operand, channel);
    }
    
    public int getPower() {
        return mmx.doCommand(NXTMMX.CMD_GETPOWER, 0, channel);
    }
    
    public void forward() {
        mmx.doCommand(NXTMMX.CMD_FORWARD, 0, channel);
    }

    public void backward() {
        mmx.doCommand(NXTMMX.CMD_BACKWARD, 0, channel);
    }

    public void stop() {
        mmx.doCommand(NXTMMX.CMD_STOP, 0, channel);
    }

    public void flt() {
        mmx.doCommand(NXTMMX.CMD_FLT, 0, channel);
    }
    
    /**
     * Return <code>true</code> if the motor is moving. Note that this method reports based on the current control
     * state (i.e. commanded to move) and not if the motor is actually moving. This means a motor may be stalled but this
     * method would return <code>true</code>.
     *
     * @return <code>true</code> if the motor is executing a movement command, <code>false</code> if stopped.
     */
    public boolean isMoving() {
        return NXTMMX.MOTPARAM_OP_TRUE==mmx.doCommand(NXTMMX.CMD_ISMOVING, 0, channel);
    }

    /* (non-Javadoc)
     * @see lejos.robotics.Encoder#getTachoCount()
     */
    public int getTachoCount() {
        return mmx.doCommand(NXTMMX.CMD_GETTACHO, 0, channel);
    }
    
    /** 
     * Reset the the tachometer count. TODO verify => Calling this method will stop any current motor action. This is imposed by the HiTechic
     * Motor Controller firmware. 
     */
    public synchronized void resetTachoCount() {
    	mmx.doCommand(NXTMMX.CMD_RESETTACHO, 0, channel);
    }
    
    /**
     * Disable or Enable internal motor controller speed regulation. Setting this to <code>true</code> will cause 
     * the motor controller firmware to adjust the motor power to compensate for changing loads in order to maintain 
     * a constant motor speed. Default at instantiation is <code>false</code>.
     * 
     * @param regulate <code>true</code> to enable regulation, <code>true</code> otherwise.
     */
    public void setRegulate(boolean regulate){
        int operand=NXTMMX.MOTPARAM_OP_FALSE;
        if (regulate) operand=NXTMMX.MOTPARAM_OP_TRUE;
        mmx.doCommand(NXTMMX.CMD_SETREGULATE, operand, channel);
    }

}
