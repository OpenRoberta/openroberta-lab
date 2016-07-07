package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;

public enum ColorSensorMode implements IColorSensorMode {
    COLOUR( "getColorSensorColour", "ColorID" ),
    RED( "getColorSensorRed", "Red" ),
    RGB( "getColorSensorRgb", "RGB" ),
    AMBIENTLIGHT( "getColorSensorAmbient", "Ambient" );

    private final String[] values;

    private ColorSensorMode(String... values) {

        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}