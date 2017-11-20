package de.fhg.iais.roberta.mode.general;

import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * All colors that are legal.
 */
public enum PickColor implements IPickColor {

    RED( 0, "#B30006" ),
    GREEN( 1, "#00642E" ),
    BLUE( 2, "#0057A6" ),
    YELLOW( 3, "#F7D117" ),
    MAGENTA( 4, "#000001" ),
    ORANGE( 5, "#000002" ),
    WHITE( 6, "#FFFFFF" ),
    BLACK( 7, "#000000" ),
    PINK( 8, "#000003" ),
    GRAY( 9, "#000004" ),
    LIGHT_GRAY( 10, "#000005" ),
    DARK_GRAY( 11, "#000006" ),
    CYAN( 12, "#000007" ),
    BROWN( 13, "#532115" ),
    NONE( -1, "#585858" );

    private final String[] values;
    private final int colorID;

    private PickColor(int colorID, String... values) {
        this.values = values;
        this.colorID = colorID;
    }

    @Override
    public int getColorID() {
        return this.colorID;
    }

    @Override
    public String getHex() {
        return this.values[0];
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    public static PickColor get(int id) {
        for ( PickColor sp : PickColor.values() ) {
            if ( sp.colorID == id ) {
                return sp;
            }
        }
        throw new DbcException("Invalid color: " + id);
    }

}
