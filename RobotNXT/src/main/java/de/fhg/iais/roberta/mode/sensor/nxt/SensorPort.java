package de.fhg.iais.roberta.mode.sensor.nxt;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPort implements ISensorPort {
    IN_1( "1", "S1" ), IN_2( "2", "S2" ), IN_3( "3", "S3" ), IN_4( "4", "S4" );

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