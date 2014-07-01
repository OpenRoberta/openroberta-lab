package de.fhg.iais.roberta.ast.syntax.sensoren;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * All keys that the brick have.
 * 
 * @author kcvejoski
 */
public enum BrickKey {
    ENTER(), UP(), DOWN(), LEFT(), RIGHT(), ESCAPE(), ANY();

    private final String[] values;

    private BrickKey(String... values) {
        this.values = values;
    }

    /**
     * get key from {@link BrickKey} from string parameter. It is possible for one key to have multiple string mappings.
     * Throws exception if the operator does not exists.
     * 
     * @param name of key on the brick
     * @return key from the enum {@link BrickKey}
     */
    public static BrickKey get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid key: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( BrickKey key : BrickKey.values() ) {
            if ( key.toString().equals(sUpper) ) {
                return key;
            }
            for ( String value : key.values ) {
                if ( sUpper.equals(value) ) {
                    return key;
                }
            }
        }
        throw new DbcException("Invalid key: " + s);
    }
}
