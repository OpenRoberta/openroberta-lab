package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;

public enum SoundSensorMode implements ISoundSensorMode {
    DEFAULT, SOUND( "sound" );
    private final String[] values;

    private SoundSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
