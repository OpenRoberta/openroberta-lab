package de.fhg.iais.roberta.factory.ev3.lejos.v0;

import de.fhg.iais.roberta.factory.ev3.lejos.EV3AbstractFactory;
import de.fhg.iais.roberta.util.RobertaProperties;

public class Factory extends EV3AbstractFactory {

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties, "EV3lejos.properties");
    }

}
