package de.fhg.iais.roberta.factory.action;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the LED colors available on the robot.
 *
 * @author kcvejoski
 */
public interface IBrickLedColor extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();
}
