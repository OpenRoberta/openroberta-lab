package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Modus implements IMode {
    ACTIVE( "OPEN" ), REST( "CLOSE" ), SIT( "SIT" );

    private final String[] values;

    private Modus(String... values) {
        this.values = values;
    }

    public static Modus get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Modus: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Modus p : Modus.values() ) {
            if ( p.toString().equals(sUpper) ) {
                return p;
            }
            for ( String value : p.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return p;
                }
            }
        }
        throw new DbcException("Invalid Modus: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}