package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

public interface IInfraredSensorMode extends IMode {
    public String[] getValues();
}
