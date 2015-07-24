package de.fhg.iais.roberta.shared;

import java.util.ArrayList;
import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain information about index location of an {@link ArrayList} that is used in Blockly blocks for operations over arrays
 *
 * @author kcvejoski
 */
public enum IndexLocation {
    FIRST(), LAST(), FROM_START( "FROMSTART" ), FROM_END( "FROMEND" ), RANDOM();
    private final String[] values;

    private IndexLocation(String... values) {
        this.values = values;
    }

    /**
     * get function from {@link IndexLocation} from string parameter. It is possible for one function to have multiple string mappings.
     * Throws exception if the operator does not exists.
     *
     * @param functName of the function
     * @return function from the enum {@link FunctionNames}
     */
    public static IndexLocation get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid function name: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( IndexLocation funct : IndexLocation.values() ) {
            if ( funct.toString().equals(sUpper) ) {
                return funct;
            }
            for ( String value : funct.values ) {
                if ( sUpper.equals(value) ) {
                    return funct;
                }
            }
        }
        throw new DbcException("Invalid function name: " + s);
    }
}
