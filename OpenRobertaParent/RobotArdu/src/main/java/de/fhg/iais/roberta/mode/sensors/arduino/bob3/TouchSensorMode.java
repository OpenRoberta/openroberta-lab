package de.fhg.iais.roberta.mode.sensors.arduino.bob3;

import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;

public enum TouchSensorMode implements ITouchSensorMode {
    BOTTOM( "3" ), MIDDLE( "2" ), TOP( "1" ), ANY( "4" );

    private final String[] values;

    private TouchSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}