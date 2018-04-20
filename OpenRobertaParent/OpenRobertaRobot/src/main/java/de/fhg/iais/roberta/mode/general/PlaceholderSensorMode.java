package de.fhg.iais.roberta.mode.general;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorMode;

public enum PlaceholderSensorMode implements ISensorMode {
    VALUE;

    private final String[] values;

    private PlaceholderSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}