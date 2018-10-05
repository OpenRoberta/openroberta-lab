package de.fhg.iais.roberta.factory.arduino.mega;

import de.fhg.iais.roberta.factory.arduino.ArduinoAbstractFactory;
import de.fhg.iais.roberta.util.RobertaProperties;

public class MegaFactory extends ArduinoAbstractFactory {

    public MegaFactory(RobertaProperties robertaProperties) {
        super(robertaProperties, "mega.properties");
    }
}