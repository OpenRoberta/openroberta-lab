package de.fhg.iais.roberta.mode.sensor.nao;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPorts implements ISensorPort {
    NO_PORT, EMPTY_PORT( "" ), LEFT( "left" ), RIGHT( "right" ), FRONT( "front" ), REAR( "rear" ), MIDDLE( "middle" ), X( "x" ), Y( "y" ), Z( "z" );

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