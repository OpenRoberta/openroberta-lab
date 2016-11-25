package de.fhg.iais.roberta.mode.sensor.nxt;

import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;

public enum GyroSensorMode implements IGyroSensorMode {
    RATE( "Rate" ), ANGLE( "Angle" ), RESET();

    private final String[] values;

    private GyroSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}