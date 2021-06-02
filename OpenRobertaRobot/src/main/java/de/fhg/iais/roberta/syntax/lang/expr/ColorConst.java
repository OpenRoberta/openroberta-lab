package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
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
public class ColorConst<V> extends Expr<V> {
    private final String hexValue;

    private ColorConst(String hexValue, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("COLOR_CONST"), properties, comment);
        Assert.isTrue(hexValue != null);
        this.hexValue = hexValue;
        setReadOnly();
    }

    /**
     * creates instance of {@link ColorConst}. This instance is read only and cannot be modified.
     *
     * @param value that the color constant will have; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ColorConst}.
     */
    public static <V> ColorConst<V> make(String hexValue, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ColorConst<>(hexValue, properties, comment);
    }

    public String getHexValueAsString() {
        return this.hexValue;
    }

    public String getHexIntAsString() {
        return this.hexValue.replaceAll("#", "0x");
    }

    public int getRedChannelInt() {
        return Integer.valueOf(this.hexValue.substring(1, 3), 16);
    }

    public int getGreenChannelInt() {
        return Integer.valueOf(this.hexValue.substring(3, 5), 16);
    }

    public int getBlueChannelInt() {
        return Integer.valueOf(this.hexValue.substring(5, 7), 16);
    }

    public String getRedChannelHex() {
        return "0x" + this.hexValue.substring(1, 3);
    }

    public String getGreenChannelHex() {
        return "0x" + this.hexValue.substring(3, 5);
    }

    public String getBlueChannelHex() {
        return "0x" + this.hexValue.substring(5, 7);
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.COLOR;
    }

    @Override
    public String toString() {
        return "ColorConst [" + this.hexValue + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String hexValue = Jaxb2Ast.extractField(fields, BlocklyConstants.COLOUR);
        return ColorConst.make(hexValue, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.COLOUR, this.hexValue);
        return jaxbDestination;
    }
}
