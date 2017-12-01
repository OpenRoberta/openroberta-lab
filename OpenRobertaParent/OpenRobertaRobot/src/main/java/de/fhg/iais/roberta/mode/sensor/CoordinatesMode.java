package de.fhg.iais.roberta.mode.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.sensor.ICoordinatesMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum CoordinatesMode implements ICoordinatesMode {
    DEFAULT, X( "X" ), Y( "Y" ), Z( "Z" );

    private final String[] values;

    private CoordinatesMode(String... values) {
        this.values = values;
    }

    public static CoordinatesMode get(String direction) {
        if ( (direction == null) || direction.isEmpty() ) {
            throw new DbcException("Invalid Coordinate: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( CoordinatesMode c : CoordinatesMode.values() ) {
            if ( c.toString().equals(sUpper) ) {
                return c;
            }
            for ( String value : c.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return c;
                }
            }
        }
        throw new DbcException("Invalid Coordinate: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
