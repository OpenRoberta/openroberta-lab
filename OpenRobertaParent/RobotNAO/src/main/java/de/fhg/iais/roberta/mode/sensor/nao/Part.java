package de.fhg.iais.roberta.mode.sensor.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Part implements IMode {
    HAND(), BUMPER();

    private final String[] values;

    private Part(String... values) {
        this.values = values;
    }

    public static Part get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Part: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Part p : Part.values() ) {
            if ( p.toString().equals(sUpper) ) {
                return p;
            }
            for ( String value : p.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return p;
                }
            }
        }
        throw new DbcException("Invalid Part: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}