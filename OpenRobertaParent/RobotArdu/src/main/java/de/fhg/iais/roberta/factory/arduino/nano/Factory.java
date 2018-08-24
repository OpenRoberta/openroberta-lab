package de.fhg.iais.roberta.factory.arduino.nano;

import de.fhg.iais.roberta.factory.arduino.ArduinoAbstractFactory;
import de.fhg.iais.roberta.util.RobertaProperties;

public class Factory extends ArduinoAbstractFactory {

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties, "nano.properties");
    }
}