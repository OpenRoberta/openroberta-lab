package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IMoistureSensorMode;

public enum MoistureSensorMode implements IMoistureSensorMode {
    VALUE();

    private final String[] values;

    private MoistureSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
