package de.fhg.iais.roberta.mode.general.arduino.bob3;

import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * All colors that are legal.
 */
public enum PickColor implements IPickColor {

    KABARED( 1, "#DD4422" ),
    BLUE( 2, "#0000FF" ),
    GREEN( 3, "#00FF00" ),
    YELLOW( 4, "#FFFF00" ),
    RED( 5, "#FF0000" ),
    WHITE( 6, "#FFFFFF" ),
    VIOLET( 7, "#6633AA" ),
    PURPLE( 8, "#FF0088" ),
    CYAN( 9, "#00FFFF" ),
    ORANGE( 10, "#FF8800" ),
    FUCHSIA( 11, "#FF00FF" ),
    AQUAMARINE( 12, "#77FFDD" ),
    CORAL( 13, "#FF7755" ),
    CORNFLOWERBLUE( 14, "#6699EE" ),
    STEELBLUE( 15, "#4488AA" ),
    ROYALBLUE( 16, "#4466EE" ),
    FORESTGREEN( 17, "#228822" ),
    SEAGREEN( 18, "#55FF99" ),
    OFF( -1, "#000000" );

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
