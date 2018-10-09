package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.RobertaProperties;

public class UnoFactory extends AbstractArduinoFactory {

    public UnoFactory(RobertaProperties robertaProperties) {
        super(robertaProperties, "uno.properties");
    }
}