package de.fhg.iais.roberta.mode.sensor.vorwerk;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;

public enum SensorPort implements ISensorPort {
    NO_PORT,
    EMPTY_PORT( "" ),
    X( "X", "Pitch" ),
    Y( "Y", "Roll" ),
    Z( "Z", "Yaw" ),
    STRENGTH( "STRENGTH" ),
    LEFT( "LEFT" ),
    RIGHT( "RIGHT" ),
    CENTER( "CENTER" ),
    LEFT_ULTRASONIC( "LEFT_ULTRASONIC" ),
    RIGHT_ULTRASONIC( "RIGHT_ULTRASONIC" ),
    CENTER_ULTRASONIC( "CENTER_ULTRASONIC" );

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
