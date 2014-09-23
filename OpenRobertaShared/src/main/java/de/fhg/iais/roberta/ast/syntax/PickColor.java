package de.fhg.iais.roberta.ast.syntax;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * All colors that are legal.
 */
public enum PickColor {
    GREEN( "GRÜN" ), RED( "ROT" ), WHITE( "WEIß", "WEISS" ), BLUE( "BLAU" );
    private final String[] values;

    private PickColor(String... values) {
        this.values = values;
    }

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    /**
     * get {@link PickColor} from string parameter. It is possible for one color to have multiple string mappings.
     * Throws exception if the color cannot be found.
     * 
     * @param name of the color
     * @return enum {@link PickColor}
     */
    public static PickColor get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Color missing");
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( PickColor sp : PickColor.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.values ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid color: " + s);
    }
}
