package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;

public enum LightSensorMode implements ILightSensorMode {
    DEFAULT, LIGHT( "Red" ), RED ("Red"), AMBIENTLIGHT( "Ambientlight" ), LEFT( "Left" ), RIGHT( "Right" ), LIGHT_VALUE(), VALUE();

    private final String[] values;

    private LightSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    public String getModeValue() {
        if ( this.values.length != 0 ) {
            return this.values[0].toUpperCase();
        } else {
            return this.toString().toUpperCase();
        }
    }
}
