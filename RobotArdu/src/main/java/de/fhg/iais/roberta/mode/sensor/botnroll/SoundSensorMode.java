package de.fhg.iais.roberta.mode.sensor.botnroll;

import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;

public enum SoundSensorMode implements ISoundSensorMode {
    SOUND( "sound", "getSample" );

    private final String[] values;

    private SoundSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}