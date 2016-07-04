package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

public interface ITimerSensorMode extends IMode {
    public String[] getValues();
}
