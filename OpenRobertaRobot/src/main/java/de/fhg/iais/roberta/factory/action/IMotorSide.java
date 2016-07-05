package de.fhg.iais.roberta.factory.action;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain robot sides where a motor can be connected. (ex. Left, Right, ...)
 *
 * @author kcvejoski
 */
public interface IMotorSide extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();

    public String getText();

}
