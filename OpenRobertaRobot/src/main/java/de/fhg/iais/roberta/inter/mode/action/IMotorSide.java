package de.fhg.iais.roberta.inter.mode.action;

import de.fhg.iais.roberta.inter.mode.general.IMode;

/**
 * The enumeration implementing this interface should contain robot sides where a motor can be connected. (ex. Left, Right, ...)
 *
 * @author kcvejoski
 */
public interface IMotorSide extends IMode {

    public String getText();

}
