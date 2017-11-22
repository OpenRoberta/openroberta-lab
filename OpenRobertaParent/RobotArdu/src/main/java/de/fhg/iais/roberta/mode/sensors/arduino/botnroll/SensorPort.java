package de.fhg.iais.roberta.mode.sensors.arduino.botnroll;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPort implements ISensorPort {
    NO_PORT(),
    S0( "0" ),
    S1( "1" ),
    S2( "2" ),
    S3( "3" ),
    S4( "4" ),
    S5( "5" ),
    S6( "6" ),
    S7( "7" ),
    S8( "8" ),
    S9( "9" ),
    S10( "10" ),
    ANY( "3" ),
    BOTH( "3" );

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