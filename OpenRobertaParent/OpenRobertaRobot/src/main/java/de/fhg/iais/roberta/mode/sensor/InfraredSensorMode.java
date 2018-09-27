package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;

public enum InfraredSensorMode implements IInfraredSensorMode {
    DEFAULT, VALUE(), DISTANCE( "Distance" ), OBSTACLE( "Distance" ), PRESENCE(), AMBIENTLIGHT(), SENSOR1( "1" ), SENSOR2( "2" ), SEEK();

    private final String[] values;

    private InfraredSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
