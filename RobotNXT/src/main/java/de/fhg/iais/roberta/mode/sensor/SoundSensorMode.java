package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;

public enum SoundSensorMode implements ISoundSensorMode {
    SOUND( "sound", "getSample" );

    private final String[] values;

    private SoundSensorMode(String... values) {
        this.values = values;
    }

    /**
     * @return name that Lejos is using for this mode
     */
    public String getLejosModeName() {
        return values[0];
    }

    @Override
    public String[] getValues() {
        return values;
    }

}