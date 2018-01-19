package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;

public enum InfraredSensorMode implements IInfraredSensorMode {
    DEFAULT, VALUE(), DISTANCE( "Distance" ), OBSTACLE( "Distance" ), PRESENCE(), AMBIENTLIGHT(), SEEK();

    private final String[] values;

    private InfraredSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}
