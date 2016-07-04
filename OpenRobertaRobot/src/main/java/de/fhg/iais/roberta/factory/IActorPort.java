package de.fhg.iais.roberta.factory;

public interface IActorPort {
    public String[] getValues();

    /**
     * @return valid Java code name of the enumeration
     */
    public String getXmlName();
}
