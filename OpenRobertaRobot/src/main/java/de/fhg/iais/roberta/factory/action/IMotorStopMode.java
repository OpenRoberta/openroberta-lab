package de.fhg.iais.roberta.factory.action;

import de.fhg.iais.roberta.factory.IMode;

/**
 * Mode in which the motor can stop.
 */
public interface IMotorStopMode extends IMode {
    public String[] getValues();
}
