package de.fhg.iais.roberta.mode.sensor.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Coordinates implements IMode {
    X(), Y(), Z();

    private final String[] values;

    private Coordinates(String... values) {
        this.values = values;
    }

    public static Coordinates get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Coordinate: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Coordinates c : Coordinates.values() ) {
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