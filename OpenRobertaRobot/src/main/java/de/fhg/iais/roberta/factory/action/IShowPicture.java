package de.fhg.iais.roberta.factory.action;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all pictures that can be displayed on a robot.
 *
 * @author kcvejoski
 */
public interface IShowPicture extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();
}
