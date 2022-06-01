package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robColour_picker</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate color
 * constant.<br/>
 * <br>
 * The client must provide the value of the color. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoOp(containerType = "COLOR_CONST", blocklyType = BlocklyType.COLOR)
public class ColorConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.COLOUR)
    public final String hexValue;

    public ColorConst(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String hexValue) {
        super(kind, properties, comment);
        Assert.isTrue(hexValue != null);
        this.hexValue = hexValue;
        setReadOnly();
    }

    /**
     * creates instance of {@link ColorConst}. This instance is read only and cannot be modified.
     *
     * @param hexValue that the color constant will have; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ColorConst}.
     */
    public static <V> ColorConst<V> make(String hexValue, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ColorConst<>(BlockTypeContainer.getByName("COLOR_CONST"), properties, comment, hexValue);
    }

    public String getBlueChannelHex() {
        return "0x" + this.hexValue.substring(5, 7);
    }

    public int getBlueChannelInt() {
        return Integer.valueOf(this.hexValue.substring(5, 7), 16);
    }

    public String getGreenChannelHex() {
        return "0x" + this.hexValue.substring(3, 5);
    }

    public int getGreenChannelInt() {
        return Integer.valueOf(this.hexValue.substring(3, 5), 16);
    }

    public String getHexIntAsString() {
        return this.hexValue.replaceAll("#", "0x");
    }

    public String getHexValueAsString() {
        return this.hexValue;
    }

    public String getRedChannelHex() {
        return "0x" + this.hexValue.substring(1, 3);
    }

    public int getRedChannelInt() {
        return Integer.valueOf(this.hexValue.substring(1, 3), 16);
    }

}
