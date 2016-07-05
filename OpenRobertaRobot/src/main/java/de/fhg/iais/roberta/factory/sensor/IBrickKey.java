package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the keys available on the robot.
 *
 * @author kcvejoski
 */
public interface IBrickKey extends IMode {
    public String[] getValues();
}
