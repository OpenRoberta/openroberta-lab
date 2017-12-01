package de.fag.iais.roberta.mode.sensor.arduino.mbot;

import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;

public enum LightSensorMode implements ILightSensorMode {
    DEFAULT(), LEFT( "Left" ), RIGHT( "Right" );

    private final String[] values;

    private LightSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}