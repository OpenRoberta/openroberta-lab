package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;

public enum LightSensorMode implements ILightSensorMode {
    RED( "getLightSensorRed", "Red" ), AMBIENTLIGHT( "getLightSensorAmbient", "Ambient" );

    private final String[] values;

    private LightSensorMode(String... values) {
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