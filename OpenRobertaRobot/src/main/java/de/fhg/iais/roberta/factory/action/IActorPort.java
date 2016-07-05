package de.fhg.iais.roberta.factory.action;

import de.fhg.iais.roberta.factory.IMode;

/**
 * The enumeration implementing this interface should contain all the actor ports available on the robot.
 *
 * @author kcvejoski
 */
public interface IActorPort extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();

    /**
     * @return the name used in the Blockly XML representation.
     */
    public String getXmlName();
}
