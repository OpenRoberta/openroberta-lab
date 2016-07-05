package de.fhg.iais.roberta.factory.action;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the drive directions of the robot in differential drive.
 *
 * @author kcvejoski
 */
public interface IDriveDirection extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();
}
