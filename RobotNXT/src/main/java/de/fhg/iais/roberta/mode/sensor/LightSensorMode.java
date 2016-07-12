package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;

public enum LightSensorMode implements IColorSensorMode {
    LIGHT( "light", "getSample" ), AMBIENTLIGHT( "getColorSensorAmbient", "Ambient" );

    private final String[] values;
    private final String halJavaMethodName;

    private LightSensorMode(String halJavaMethodName, String... values) {
        this.halJavaMethodName = halJavaMethodName;
        this.values = values;
    }

    /**
     * @return name that Lejos is using for this mode
     */
    public String getLejosModeName() {
        return this.values[0];
    }

    //    @Override
    //    public String getHalJavaMethod() {
    //        return this.halJavaMethodName;
    //    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}