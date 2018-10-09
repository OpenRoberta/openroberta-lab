package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.util.RobertaProperties;

public class NanoFactory extends AbstractArduinoFactory {

    public NanoFactory(RobertaProperties robertaProperties) {
        super(robertaProperties, "nano.properties");
    }
}