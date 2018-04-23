package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ISlot;

public enum Slot implements ISlot {
    NO_SLOT,
    EMPTY_SLOT( "" ),
    SENSOR_TOP( "1" ),
    CENTER( "2" ),
    SENSOR_BOTTOM( "3" ),
    SENSOR_ANY( "0" ),
    LEFT( "LEFT" ),
    RIGHT( "RIGHT" ),
    FRONT( "FRONT" ),
    REAR( "REAR" ),
    MIDDLE( "MIDDLE" ),
    YAW( "YAW" ),
    PITCH( "PITCH" ),
    LEFT_PITCH( "LEFT_PITCH" ),
    LEFT_ROLL( "LEFT_ROLL" ),
    LEFT_YAW( "LEFT_YAW" ),
    LEFT_YAW_PITCH( "LEFT_YAW_PITCH" ),
    RIGHT_PITCH( "RIGHT_PITCH" ),
    RIGHT_ROLL( "RIGHT_ROLL" ),
    RIGHT_YAW( "RIGHT_YAW" ),
    RIGHT_YAW_PITCH( "RIGHT_YAW_PITCH" );
    private final String[] values;

    private Slot(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
