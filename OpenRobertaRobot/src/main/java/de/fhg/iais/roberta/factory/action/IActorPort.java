package de.fhg.iais.roberta.factory.action;

import de.fhg.iais.roberta.factory.IMode;

public interface IActorPort extends IMode {
    public String[] getValues();

    /**
     * @return valid Java code name of the enumeration
     */
    public String getXmlName();
}
