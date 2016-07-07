package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;

public enum UltrasonicSensorMode implements IUltrasonicSensorMode {
    DISTANCE( "getUltraSonicSensorDistance", "Distance" ), PRESENCE( "getUltraSonicSensorPresence", "Listen" );

    private final String[] values;

    private UltrasonicSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}