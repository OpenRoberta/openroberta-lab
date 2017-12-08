package de.fhg.iais.roberta.mode.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.sensor.ICoordinatesMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Axis implements ICoordinatesMode, IJoystickMode, IGyroSensorMode {
    DEFAULT, X( "X" ), Y( "Y" ), Z( "Z" );

    private final String[] values;

    private Axis(String... values) {
        this.values = values;
    }

    public static Axis get(String axis) {
        if ( (axis == null) || axis.isEmpty() ) {
            throw new DbcException("Invalid Coordinate: " + axis);
        }
        String sUpper = axis.trim().toUpperCase(Locale.GERMAN);
        for ( Axis c : Axis.values() ) {
            if ( c.toString().equals(sUpper) ) {
                return c;
            }
            for ( String value : c.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return c;
                }
            }
        }
        throw new DbcException("Invalid Coordinate: " + axis);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
