package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

public interface IUltrasonicSensorMode extends IMode {
    public String[] getValues();
}
