package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Resolution implements IMode {
    LOW( "0" ), MED( "1" ), HIGH( "2" );

    private final String[] values;

    private Resolution(String... values) {
        this.values = values;
    }

    public static Resolution get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Resolution: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Resolution r : Resolution.values() ) {
            if ( r.toString().equals(sUpper) ) {
                return r;
            }
            for ( String value : r.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return r;
                }
            }
        }
        throw new DbcException("Invalid Resolution: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}