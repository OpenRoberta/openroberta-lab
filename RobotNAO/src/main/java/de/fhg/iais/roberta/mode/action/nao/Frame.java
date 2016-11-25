package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Frame implements IMode {
    TORSO( "0" ), WORLD( "1" ), ROBOT( "2" );

    private final String[] values;

    private Frame(String... values) {
        this.values = values;
    }

    public static Frame get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Posture: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Frame f : Frame.values() ) {
            if ( f.toString().equals(sUpper) ) {
                return f;
            }
            for ( String value : f.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return f;
                }
            }
        }
        throw new DbcException("Invalid Posture: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}