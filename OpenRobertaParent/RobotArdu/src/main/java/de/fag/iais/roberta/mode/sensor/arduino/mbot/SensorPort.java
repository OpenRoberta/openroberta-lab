package de.fag.iais.roberta.mode.sensor.arduino.mbot;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPort implements ISensorPort {
    PORT_0( "0", "P0" ), PORT_1( "1", "P1" ), PORT_2( "2", "P2" ), PORT_3( "3", "P3" ), PORT_4( "4", "P4" );

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