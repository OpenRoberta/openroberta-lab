package de.fhg.iais.roberta.mode.sensor.botnroll;

import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;

public enum LightSensorMode implements ILightSensorMode {
    RED( "light", "getSample" ), AMBIENTLIGHT( "getColorSensorAmbient", "Ambient" );

    private final String[] values;

    private LightSensorMode(String... values) {
        this.values = values;
    }

    /**
     * @return name that Lejos is using for this mode
     */
    public String getLejosModeName() {
        return this.values[0];
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}