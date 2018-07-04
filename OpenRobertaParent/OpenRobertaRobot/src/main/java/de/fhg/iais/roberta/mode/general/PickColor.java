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
    NONE( -1, "#585858" ),
    W_PINK( 1, "#FF1493" ),
    W_PURPLE( 2, "#800080" ),
    W_BLUE( 3, "#4876FF" ),
    W_CYAN( 4, "#00FFFF" ),
    W_LIGHTGREEN( 5, "#90EE90" ),
    W_GREEN( 6, "#008000" ),
    W_YELLOW( 7, "#FFFF00" ),
    W_ORANGE( 8, "#FFA500" ),
    W_RED( 9, "#FF0000" ),
    W_WHITE( 10, "#FFFFFE" );

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
