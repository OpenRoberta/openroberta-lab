package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ITemperatureSensorMode;

public enum TemperatureSensorMode implements ITemperatureSensorMode {
    DEFAULT(), VALUE();

    private final String[] values;

    private TemperatureSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
