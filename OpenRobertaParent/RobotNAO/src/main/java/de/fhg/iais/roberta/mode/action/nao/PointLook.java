package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum PointLook implements IMode {
    LOOK( "1" ), POINT( "0" );

    private final String[] values;

    private PointLook(String... values) {
        this.values = values;
    }

    public static PointLook get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Modus: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( PointLook p : PointLook.values() ) {
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