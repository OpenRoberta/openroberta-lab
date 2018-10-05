package de.fhg.iais.roberta.factory.arduino.uno;

import de.fhg.iais.roberta.factory.arduino.ArduinoAbstractFactory;
import de.fhg.iais.roberta.util.RobertaProperties;

public class UnoFactory extends ArduinoAbstractFactory {

    public UnoFactory(RobertaProperties robertaProperties) {
        super(robertaProperties, "uno.properties");
    }
}