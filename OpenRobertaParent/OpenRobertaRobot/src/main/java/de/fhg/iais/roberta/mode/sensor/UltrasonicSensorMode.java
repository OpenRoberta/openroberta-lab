package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;

public enum UltrasonicSensorMode implements IUltrasonicSensorMode {
    DEFAULT, DISTANCE( "Distance" ), PRESENCE( "Listen" );

    private final String[] values;

    private UltrasonicSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}