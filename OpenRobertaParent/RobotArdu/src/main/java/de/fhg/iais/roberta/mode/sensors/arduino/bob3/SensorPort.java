package de.fhg.iais.roberta.mode.sensors.arduino.bob3;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPort implements ISensorPort {
    LEFT( "1" ), RIGHT( "2" );

    private final String[] values;

    private SensorPort(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    @Override
    public String getPortNumber() {
        return this.values[0];
    }

}