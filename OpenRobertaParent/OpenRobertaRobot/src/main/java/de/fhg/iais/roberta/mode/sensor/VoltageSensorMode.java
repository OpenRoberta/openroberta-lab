package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IVoltageSensorMode;

public enum VoltageSensorMode implements IVoltageSensorMode {
    VALUE();

    private final String[] values;

    private VoltageSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
