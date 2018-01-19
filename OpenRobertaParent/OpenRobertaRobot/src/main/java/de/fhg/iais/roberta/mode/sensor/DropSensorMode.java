package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IDropSensorMode;

public enum DropSensorMode implements IDropSensorMode {
    VALUE();

    private final String[] values;

    private DropSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
