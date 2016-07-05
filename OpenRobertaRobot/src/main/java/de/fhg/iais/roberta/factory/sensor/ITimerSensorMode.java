package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the timer sensor modes available on the robot.
 *
 * @author kcvejoski
 */
public interface ITimerSensorMode extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();
}
