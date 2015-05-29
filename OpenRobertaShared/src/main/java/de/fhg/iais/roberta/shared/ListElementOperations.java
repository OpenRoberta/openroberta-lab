package de.fhg.iais.roberta.shared;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contains all the operations that can be performed over an element in list.
 *
 * @author kcvejoski
 */
public enum ListElementOperations {
    GET( false ), GET_REMOVE( false ), REMOVE( true ), SET( true ), INSERT( true );

    private final String[] values;
    private final boolean statment;

    private ListElementOperations(boolean statment, String... strings) {
        this.values = strings;
        this.statment = statment;
    }

    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * get mode from {@link ListElementOperations} from string parameter. It is possible for one function to have multiple string mappings.
     * Throws exception if the operator does not exists.
     *
     * @param location of the function
     * @return function from the enum {@link ListElementOperations}
     */
    public static ListElementOperations get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid mode name: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( ListElementOperations funct : ListElementOperations.values() ) {
            if ( funct.toString().equals(sUpper) ) {
                return funct;
            }
            for ( String value : funct.values ) {
                if ( sUpper.equals(value) ) {
                    return funct;
                }
            }
        }
        throw new DbcException("Invalid mode name: " + s);
    }

    /**
     * @return true if the operation does not return value
     */
    public boolean isStatment() {
        return this.statment;
    }
}
