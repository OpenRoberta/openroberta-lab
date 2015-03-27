package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;

/**
 * This class represents the <b>logic_boolean</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate boolean constant.<br/>
 * <br>
 * The client must provide the value of the boolean constant. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(boolean, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class BoolConst<V> extends Expr<V> {
    private final boolean value;

    private BoolConst(boolean value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.BOOL_CONST, properties, comment);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link BoolConst}. This instance is read only and can not be modified.
     *
     * @param value that the boolean constant will have,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BoolConst}
     */
    public static <V> BoolConst<V> make(boolean value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BoolConst<V>(value, properties, comment);
    }

    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String field = helper.extractField(fields, BlocklyConstants.BOOL);
        return BoolConst.make(Boolean.parseBoolean(field.toLowerCase()), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    /**
     * @return the value of the boolean constant.
     */
    public boolean isValue() {
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
        return "BoolConst [" + this.value + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBoolConst(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = String.valueOf(((BoolConst<?>) this).isValue()).toUpperCase();
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.BOOL, fieldValue);
        return jaxbDestination;
    }
}
