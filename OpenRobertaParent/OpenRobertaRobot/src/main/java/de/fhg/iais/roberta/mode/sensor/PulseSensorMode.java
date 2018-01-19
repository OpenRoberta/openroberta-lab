package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IPulseSensorMode;

public enum PulseSensorMode implements IPulseSensorMode {
    VALUE();

    private final String[] values;

    private PulseSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
