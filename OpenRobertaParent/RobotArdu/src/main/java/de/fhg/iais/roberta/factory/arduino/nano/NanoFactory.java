package de.fhg.iais.roberta.factory.arduino.nano;

import de.fhg.iais.roberta.factory.arduino.ArduinoAbstractFactory;
import de.fhg.iais.roberta.util.RobertaProperties;

public class NanoFactory extends ArduinoAbstractFactory {

    public NanoFactory(RobertaProperties robertaProperties) {
        super(robertaProperties, "nano.properties");
    }
}