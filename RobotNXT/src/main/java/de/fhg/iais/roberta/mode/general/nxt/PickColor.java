package de.fhg.iais.roberta.mode.general.nxt;

import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * All colors that are legal.
 */
public enum PickColor implements IPickColor {

    BLACK( 1, "#000000" ),
    BLUE( 2, "#0057A6" ),
    GREEN( 3, "#00642E" ),
    YELLOW( 4, "#F7D117" ),
    RED( 5, "#B30006" ),
    WHITE( 6, "#FFFFFF" ),
    MAGENTA( 7, "#EE82EE" ),
    ORANGE( 8, "#FFA500" ),
    LIME( 9, "#7FFF00" ),
    VIOLET( 10, "#D02090" ),
    CRIMSON( 11, "#FF00FF" ),
    PURPLE( 12, "#FF00FF" ),
    NONE( -1, "#585858" ),
    BROWN( 13, "#532115" );

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
