package de.fhg.iais.roberta.inter.mode.action;

import de.fhg.iais.roberta.inter.mode.general.IMode;

/**
 * The enumeration implementing this interface should contain all the LED blinking modes available on the robot.
 *
 * @author kcvejoski
 */
public interface IBlinkMode extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();
}
