package de.fhg.iais.roberta.shared.action.ev3;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contains all the actor ports that brick can have.
 */
public enum ActorPort {
    A( "MA" ), B( "MB" ), C( "MC" ), D( "MD" );

    private final String xmlName;

    private ActorPort(String xmlName) {
        this.xmlName = xmlName;
    }

    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * @return valid Java code name of the enumeration
     */
    public String getXmlName() {
        return this.xmlName;
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
            if ( ap.xmlName.equals(sUpper) ) {
                return ap;
            }
        }
        throw new DbcException("Invalid actor port: " + port);
    }
}
