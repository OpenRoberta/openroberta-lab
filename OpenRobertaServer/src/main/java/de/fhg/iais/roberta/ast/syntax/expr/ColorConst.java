package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Pickcolor;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robColour_picker</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate color constant.<br/>
 * <br>
 * The client must provide the value of the color. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ColorConst<V> extends Expr<V> {
    private final Pickcolor value;

    private ColorConst(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.PICK_COLOR_CONST, properties, comment);
        Assert.isTrue(!value.equals(""));
        this.value = Pickcolor.get(value);
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
    public static <V> ColorConst<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ColorConst<V>(value, properties, comment);
    }

    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String field = helper.extractField(fields, BlocklyConstants.COLOUR);
        return ColorConst.make(field, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    /**
     * @return the value of the string constant.
     */
    public Pickcolor getValue() {
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
    public String toString() {
        return "ColorConst [" + this.value + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitColorConst(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COLOUR, getValue().getHex().toLowerCase());
        return jaxbDestination;
    }
}
