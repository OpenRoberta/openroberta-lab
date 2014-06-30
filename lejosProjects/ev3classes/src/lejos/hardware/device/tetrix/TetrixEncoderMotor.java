package lejos.hardware.device.tetrix;

import lejos.robotics.Encoder;
import lejos.utility.Delay;


/** 
 * Tetrix DC motor abstraction with encoder support. The Tetrix motor must have an encoder installed and connected to
 * the controller for the methods in this class to work. If an encoder is not installed, use the <code>{@link TetrixMotor}</code>
 * class instead.
 * <p>Use <code>{@link TetrixMotorController#getEncoderMotor}</code> to retrieve a <code>TetrixEncoderMotor</code> instance.
 * 
 * @author Kirk P. Thompson
 */
public class TetrixEncoderMotor extends TetrixMotor implements Encoder{
    TetrixEncoderMotor(TetrixMotorController mc, int channel) {
        super(mc, channel);
    }
    
    public int getTachoCount() {
        return (int)(mc.doCommand(TetrixMotorController.CMD_GETTACHO, 0, channel) * .25);
    }
    
    /** 
     * Reset the the tachometer count. Calling this method will stop any current motor action. This is imposed by the HiTechic
     * Motor Controller firmware. 
     */
    public synchronized void resetTachoCount() {
        mc.doCommand(TetrixMotorController.CMD_RESETTACHO, 0, channel);
    }
   
    synchronized void waitRotateComplete() {
        while (isMoving()) {
            Delay.msDelay(50);
        }
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
        mc.doCommand(TetrixMotorController.CMD_ROTATE, degrees, channel);
        if (!immediateReturn) mc.waitRotateComplete(channel);
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
        mc.doCommand(TetrixMotorController.CMD_ROTATE_TO, limitAngle, channel);
        if (!immediateReturn) mc.waitRotateComplete(channel);
    }

    /**
     * Return the last angle that this motor was rotating to via one of the rotate methods.
     * @return angle in degrees
     */
    public int getLimitAngle() {
        return mc.doCommand(TetrixMotorController.CMD_GETLIMITANGLE, 0, channel);
    }
    
    /**
     * Disable or Enable internal motor controller speed regulation. Setting this to <code>true</code> will cause 
     * the motor controller firmware to adjust the motor power to compensate for changing loads in order to maintain 
     * a constant motor speed. Default at instantiation is <code>false</code>.
     * 
     * @param regulate <code>true</code> to enable regulation, <code>false</code> otherwise.
     */
    public void setRegulate(boolean regulate){
        int operand=TetrixMotorController.MOTPARAM_OP_FALSE;
        if (regulate) operand=TetrixMotorController.MOTPARAM_OP_TRUE;
        mc.doCommand(TetrixMotorController.CMD_SETREGULATE, operand, channel);
    }
}
