package de.fhg.iais.roberta.mode.sensor.botnroll;

import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;

public enum ColorSensorMode implements IColorSensorMode {
    //TODO: after block is fixed, delete RED, RGB and AMBIENTLIGHT
    COLOUR( "getColorSensorColour", "ColorID" ),
    RED( "getColorSensorRed", "Red" ),
    RGB( "getColorSensorRgb", "RGB" ),
    AMBIENTLIGHT( "getColorSensorAmbient", "Ambient" );

    private final String[] values;
    //private final String halJavaMethodName;

    private ColorSensorMode(String halJavaMethodName, String... values) {
        //this.halJavaMethodName = halJavaMethodName;
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