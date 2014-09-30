package de.fhg.iais.roberta.ast.syntax;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * All colors that are legal.
 */
public enum PickColor {

    RED( 0, "#B30006", "ROT" ), GREEN( 1, "#00642E", "GRÜN" ), BLUE( 2, "#0057A6" ), YELLOW( 3, "#F7D117" ), MAGENTA( 4 ), ORANGE( 5 ), WHITE(
        6,
        "#FFFFFF",
        "WEIß",
        "WEISS" ), BLACK( 7, "#000000" ), PINK( 8 ), GRAY( 9 ), LIGHT_GRAY( 10 ), DARK_GRAY( 11 ), CYAN( 12 ), BROWN( 13, "#532115" ), NONE( -1, "#585858" );

    private final String[] values;
    private final int colorID;

    private PickColor(int colorID, String... values) {
        this.values = values;
        this.colorID = colorID;
    }

    public int getColorID() {
        return this.colorID;
    }

    public String getJavaCode() {
        return String.valueOf(this.colorID);
        // return this.getClass().getSimpleName() + "." + this;
    }

    public PickColor get(int id) {
        for ( PickColor sp : PickColor.values() ) {
            if ( sp.colorID == id ) {
                return sp;
            }
        }
        throw new DbcException("Invalid color: " + id);
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
