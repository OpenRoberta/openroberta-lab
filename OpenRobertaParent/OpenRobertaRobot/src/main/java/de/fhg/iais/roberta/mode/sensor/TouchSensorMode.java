package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;

public enum TouchSensorMode implements ITouchSensorMode {
    DEFAULT(), TOUCH( "touch" );
    private final String[] values;

    private TouchSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}