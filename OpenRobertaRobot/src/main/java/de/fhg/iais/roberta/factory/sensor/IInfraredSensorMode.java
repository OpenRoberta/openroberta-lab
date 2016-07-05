package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the infrared sensor modes available on the robot.
 *
 * @author kcvejoski
 */
public interface IInfraredSensorMode extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();
}
