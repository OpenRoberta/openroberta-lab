package de.fhg.iais.roberta.factory.sensor;

import de.fhg.iais.roberta.factory.IMode;

public interface ISensorPort extends IMode {
    public String[] getValues();

    public String getPortNumber();
}
