package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * All possible colors of the lights that the brick have.
 */
public enum BrickLedColor {
    GREEN(), ORANGE(), RED();

    private final String[] values;

    private BrickLedColor(String... values) {
        this.values = values;
    }

    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * get color from {@link BrickLedColor} from string parameter. It is possible for one color to have multiple string mappings.
     * Throws exception if the color does not exists.
     * 
     * @param name of the color
     * @return color from the enum {@link BrickLedColor}
     */
    public static BrickLedColor get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid color: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( BrickLedColor co : BrickLedColor.values() ) {
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