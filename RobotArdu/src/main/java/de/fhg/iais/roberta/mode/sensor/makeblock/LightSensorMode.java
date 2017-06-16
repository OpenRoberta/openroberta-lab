package de.fhg.iais.roberta.mode.sensor.makeblock;

import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;

public enum LightSensorMode implements ILightSensorMode {
    RED( "Red" ), AMBIENTLIGHT( "Ambient" );

    private final String[] values;

    private LightSensorMode(String... values) {

        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}