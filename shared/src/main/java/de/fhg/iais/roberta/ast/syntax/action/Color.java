package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * All possible colors of the lights that the brick have.
 */
public enum Color {
    GREEN(), ORANGE(), RED();

    private final String[] values;

    private Color(String... values) {
        this.values = values;
    }

    /**
     * get color from {@link Color} from string parameter. It is possible for one color to have multiple string mappings.
     * Throws exception if the color does not exists.
     * 
     * @param name of the color
     * @return color from the enum {@link Color}
     */
    public static Color get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid color: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( Color co : Color.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return co;
            }
            for ( String value : co.values ) {
                if ( sUpper.equals(value) ) {
                    return co;
                }
            }
        }
        throw new DbcException("Invalid color: " + s);
    }
}