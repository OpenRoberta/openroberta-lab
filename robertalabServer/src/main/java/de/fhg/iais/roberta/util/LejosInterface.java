package de.fhg.iais.roberta.util;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.action.MotorDuration;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;

public class LejosInterface {

    /**
     * Set the speed of the motor connected to port {@link port} and start if {@link activate} is true.
     * This call terminate immediately and keep the motor on if {@link mDuration} is null.
     * Otherwise it will wait until {@link mDuration} becomes satisfied and stop the motor.
     * 
     * @param port
     * @param speedPercent
     * @param mDuration
     * @param activate
     * @return
     */
    public boolean setMotor(ActorPort port, int speedPercent, MotorDuration mDuration, boolean activate) {
        return true;
    }

    /**
     * Stop the motor connected to port {@link port} if {@link activate} is true.
     * This call set the motor mode (see {@link MotorStopMode}) and stops the motor if {@link activate} is true.
     * 
     * @param port
     * @param mMode
     * @param activate
     * @return
     */
    public boolean setMotorStop(ActorPort port, MotorStopMode mMode, boolean activate) {
        return true;
    }

    /**
     * Get the speed from a motor connected to {@link port}.
     * 
     * @param port
     * @return
     */
    public int getMotorSpeed(ActorPort port) {
        return 30;
    }

    /**
     * Is touch sensor connected to {@link port} pressed?.
     * 
     * @param port
     * @return
     */
    public boolean isTouchSensorPressed(SensorPort port) {
        return false;
    }

}
