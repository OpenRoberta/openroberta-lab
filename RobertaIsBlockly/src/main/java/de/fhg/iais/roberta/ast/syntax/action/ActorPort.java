package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

public enum ActorPort {
    A(), B(), C(), D();

    private final String[] values;

    private ActorPort(String... values) {
        this.values = values;
    }

    public static ActorPort get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( ActorPort ap : ActorPort.values() ) {
            if ( ap.toString().equals(sUpper) ) {
                return ap;
            }
            for ( String value : ap.values ) {
                if ( sUpper.equals(value) ) {
                    return ap;
                }
            }
        }
        throw new DbcException("Invalid binary operator symbol: " + s);
    }
}
