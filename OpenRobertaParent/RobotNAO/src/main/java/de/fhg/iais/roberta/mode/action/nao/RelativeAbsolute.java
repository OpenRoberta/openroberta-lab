package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum RelativeAbsolute implements IMode {
    RELATIVE( "0" ), ABSOLUTE( "1" );

    private final String[] values;

    private RelativeAbsolute(String... values) {
        this.values = values;
    }

    public static RelativeAbsolute get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Mode: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( RelativeAbsolute c : RelativeAbsolute.values() ) {
            if ( c.toString().equals(sUpper) ) {
                return c;
            }
            for ( String value : c.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return c;
                }
            }
        }
        throw new DbcException("Invalid Mode: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
