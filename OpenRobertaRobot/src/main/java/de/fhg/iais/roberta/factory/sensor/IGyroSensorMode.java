package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the gyro sensor modes available on the robot.
 *
 * @author kcvejoski
 */
public interface IGyroSensorMode extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();
}
