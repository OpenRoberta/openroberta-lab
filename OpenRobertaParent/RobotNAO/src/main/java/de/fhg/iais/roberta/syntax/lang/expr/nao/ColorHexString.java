package de.fhg.iais.roberta.syntax.lang.expr.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>robColour_picker</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate color
 * constant.<br/>
 * <br>
 * The client must provide the value of the color. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ColorHexString<V> extends Expr<V> {
    private final String value;

    private ColorHexString(String color, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("COLOR_HEX_STRING"), properties, comment);
        Assert.isTrue(color != null);
        this.value = color;
        setReadOnly();
    }

    /**
     * creates instance of {@link ColorHexString}. This instance is read only and cannot be modified.
     *
     * @param value that the color constant will have; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ColorHexString}.
     */
    public static <V> ColorHexString<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ColorHexString<V>(value, properties, comment);
    }

    /**
     * @return the value of the string constant.
     */
    public String getValue() {
        return this.value;
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
        return "ColorHexString [" + this.value + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitColorHexString(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String field = helper.extractField(fields, BlocklyConstants.COLOUR);
        return ColorHexString.make(field, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COLOUR, getValue().toLowerCase());
        return jaxbDestination;
    }
}
