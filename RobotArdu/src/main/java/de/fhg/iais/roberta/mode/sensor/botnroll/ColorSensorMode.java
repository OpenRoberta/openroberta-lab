package de.fhg.iais.roberta.mode.sensor.botnroll;

import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;

public enum ColorSensorMode implements IColorSensorMode {
    COLOUR( "getColorSensorColour", "ColorID" ), RED( "getColorSensorRed", "Red" ), RGB( "getColorSensorRgb", "RGB" );

    private final String[] values;

    private ColorSensorMode(String halJavaMethodName, String... values) {
        this.values = values;
    }

    /**
     * @return name that Lejos is using for this mode
     */

    @Override
    public String[] getValues() {
        return this.values;
    }

}