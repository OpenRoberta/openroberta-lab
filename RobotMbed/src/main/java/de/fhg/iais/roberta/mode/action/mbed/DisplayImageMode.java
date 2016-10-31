package de.fhg.iais.roberta.mode.action.mbed;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

public enum DisplayImageMode {
    IMAGE(), ANIMATION();

    private final String[] values;

    private DisplayImageMode(String... values) {
        this.values = values;
    }

    public static DisplayImageMode get(String mode) {
        if ( mode == null || mode.isEmpty() ) {
            throw new DbcException("Invalid Display Image Mode: " + mode);
        }
        String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        for ( DisplayImageMode mo : DisplayImageMode.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.values ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid Display Image Mode: " + mode);
    }

}
