package de.fhg.iais.roberta.mode.sensor.nxt;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPort implements ISensorPort {
    S1( "1" ), S2( "2" ), S3( "3" ), S4( "4" );

    private final String[] values;

    private SensorPort(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return values;
    }

    @Override
    public String getPortNumber() {
        return values[0];
    }
}