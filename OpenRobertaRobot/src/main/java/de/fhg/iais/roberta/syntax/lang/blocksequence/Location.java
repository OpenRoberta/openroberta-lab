package de.fhg.iais.roberta.syntax.lang.blocksequence;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;

/**
 * This class stores the information of the location of the top block in a blockly program.
 *
 * @author kcvejoski
 */
@NepoBasic(containerType = "LOCATION", category = "HELPER", blocklyNames = {})
public final class Location<V> extends Task<V> {
    public final String x;
    public final String y;

    public Location(String x, String y) {
        super(BlocklyBlockProperties.make("t", "t", true, false, false, false, false, true, false, false), null);
        Assert.isTrue(!x.equals("") && !y.equals(""));
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    /**
     * creates instance of {@link Location}. This instance is read only and cannot be modified.
     *
     * @param x coordinate of the position of the block, must be <b>non-empty</b> string,
     * @param y coordinate of the position of the block, must be <b>non-empty</b> string,
     * @return read only object of class {@link Location}
     */
    public static <V> Location<V> make(String x, String y) {
        return new Location<V>(x, y);
    }

    /**
     * @return x coordinate of the block position
     */
    public String getX() {
        return this.x;
    }

    /**
     * @return y coordinate of the block position
     */
    public String getY() {
        return this.y;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public Assoc getAssoc() {
        return null;
    }

    @Override
    public String toString() {
        return "Location [x=" + this.x + ", y=" + this.y + "]";
    }

    @Override
    public Block astToBlock() {
        return null;
    }

}
