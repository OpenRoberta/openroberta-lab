package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoExpr(category = "EXPR", blocklyNames = {"naoColour_picker", "robColour_picker", "mbedColour_picker"}, name = "COLOR_CONST", blocklyType = BlocklyType.COLOR)
public final class ColorConst<V> extends Expr<V> {
    @NepoField(name = "COLOUR")
    public final String hexValue;

    public ColorConst(BlocklyBlockProperties properties, BlocklyComment comment, String hexValue) {
        super(properties, comment);
        Assert.isTrue(hexValue != null);
        this.hexValue = hexValue;
        setReadOnly();
    }

    public static <V> ColorConst<V> make(String hexValue, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ColorConst<>(properties, comment, hexValue);
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
