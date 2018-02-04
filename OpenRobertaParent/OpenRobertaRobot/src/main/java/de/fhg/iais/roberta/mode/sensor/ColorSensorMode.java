package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;

public enum ColorSensorMode implements IColorSensorMode {
    DEFAULT, COLOUR( "Colour" ), RED ("Red"), RGB( "RGB" ), AMBIENTLIGHT( "Ambientlight" ), LIGHT( "Red" );

    private final String[] values;

    private ColorSensorMode(String... values) {
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