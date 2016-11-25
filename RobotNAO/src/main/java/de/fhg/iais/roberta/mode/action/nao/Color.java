package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Color implements IMode {
    GREEN(), RED(), BLUE();

    private final String[] values;

    private Color(String... values) {
        this.values = values;
    }

    public static Color get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Posture: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Color c : Color.values() ) {
            if ( c.toString().equals(sUpper) ) {
                return c;
            }
            for ( String value : c.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return c;
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