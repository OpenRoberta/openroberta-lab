package de.fhg.iais.roberta.mode.sensor.makeblock;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPort implements ISensorPort {
    PORT_1( "1", "S1" ), PORT_2( "2", "S2" ), PORT_3( "3", "S3" ), PORT_4( "4", "S4" );

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