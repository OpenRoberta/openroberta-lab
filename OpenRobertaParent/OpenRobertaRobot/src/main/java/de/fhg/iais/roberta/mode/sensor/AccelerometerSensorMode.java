package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IAccelerometerSensorMode;

public enum AccelerometerSensorMode implements IAccelerometerSensorMode {
    DEFAULT, X( "X" ), Y( "Y" ), Z( "Z" );

    private final String[] values;

    private AccelerometerSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return values;
    }

}
