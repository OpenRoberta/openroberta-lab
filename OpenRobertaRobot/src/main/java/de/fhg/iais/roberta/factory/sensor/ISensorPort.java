package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the sensor ports available on the robot.
 *
 * @author kcvejoski
 */
public interface ISensorPort extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();

    /**
     * @return the number of the port
     */
    public String getPortNumber();
}
