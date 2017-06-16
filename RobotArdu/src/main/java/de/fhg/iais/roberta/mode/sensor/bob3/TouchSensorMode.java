package de.fhg.iais.roberta.mode.sensor.bob3;

import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;

public enum TouchSensorMode implements ITouchSensorMode {
    UPPER( "upper" ), MIDDLE( "middle" ), LOWER( "LOWER" );

    private final String[] values;

    private TouchSensorMode(String... values) {

        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}