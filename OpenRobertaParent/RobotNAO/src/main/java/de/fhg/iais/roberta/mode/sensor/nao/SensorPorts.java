package de.fhg.iais.roberta.mode.sensor.nao;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPorts implements ISensorPort {
    NO_PORT,
    EMPTY_PORT( "" ),
    X( "x" ),
    Y( "y" ),
    Z( "z" ),
    HEAD( "HEAD" ),
    HAND( "HAND" ),
    SHOULDER( "SHOULDER" ),
    ELBOW( "ELBOW" ),
    WRIST( "WRIST" ),
    HIP( "HIP" ),
    KNEE( "KNEE" ),
    ANKLE( "ANKLE" ),
    BUMPER( "BUMPER" ),
    LEFT( "LEFT" ),
    RIGHT( "RIGHT" );

    private final String[] values;

    private SensorPorts(String... values) {
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