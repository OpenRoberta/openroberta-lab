package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;

public enum GyroSensorMode implements IGyroSensorMode {
    RATE( "getGyroSensorRate", "Rate" ), ANGLE( "getGyroSensorAngle", "Angle" ), RESET( "resetGyroSensor" );

    private final String[] values;

    private GyroSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}