package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Camera implements IMode {
    TOP( "0" ), BOTTOM( "1" );

    private final String[] values;

    private Camera(String... values) {
        this.values = values;
    }

    public static Camera get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Camera: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Camera c : Camera.values() ) {
            if ( c.toString().equals(sUpper) ) {
                return c;
            }
            for ( String value : c.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return c;
                }
            }
        }
        throw new DbcException("Invalid Camera: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
