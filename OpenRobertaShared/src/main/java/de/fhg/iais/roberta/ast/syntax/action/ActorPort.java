package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This enumeration contains all the actor ports that brick can have.
 */
public enum ActorPort {
    A(), B(), C(), D();

    private final String[] values;

    private ActorPort(String... values) {
        this.values = values;
    }

    /**
     * get actor port from {@link ActorPort} from string parameter. It is possible for actor port to have multiple string mappings.
     * Throws exception if the actor port does not exists.
     * 
     * @param name of the actor port
     * @return actor port from the enum {@link ActorPort}
     */
    public static ActorPort get(String port) {
        if ( port == null || port.isEmpty() ) {
            throw new DbcException("Invalid actor port: " + port);
        }
        String sUpper = port.trim().toUpperCase(Locale.GERMAN);
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
        throw new DbcException("Invalid actor port: " + port);
    }
}
