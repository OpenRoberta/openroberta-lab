package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

public interface IGyroSensorMode extends IMode {
    public String[] getValues();
}
