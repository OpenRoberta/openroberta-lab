package de.fhg.iais.roberta.mode.action.mbed;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

public enum DisplayTextMode {
    TEXT(), CHARACTER();

    private final String[] values;

    private DisplayTextMode(String... values) {
        this.values = values;
    }

    public static DisplayTextMode get(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Display Text Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( DisplayTextMode mo : DisplayTextMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.values ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Display Text Mode: " + mode);
    }

}
