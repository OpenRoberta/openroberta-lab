package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

public interface ITouchSensorMode extends IMode {
    public String[] getValues();
}
