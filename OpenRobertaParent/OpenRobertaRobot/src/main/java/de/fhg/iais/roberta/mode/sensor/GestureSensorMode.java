package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IGestureSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;

public enum GestureSensorMode implements IGestureSensorMode {
    VALUE,
    UP( "up" ),
    DOWN( "down" ),
    LEFT( "left" ),
    RIGHT( "righ" ),
    FACE_DOWN( "facedown" ),
    FACE_UP( "faceup" ),
    SHAKE( "shake" ),
    FREEFALL( "freefall" ),
    G3( "3g" ),
    G6( "6g" ),
    G8( "8g" );

    private final String[] values;

    private GestureSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}