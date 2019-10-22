package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the blockly block for constant numbers in the AST . Object from this class represent one read-only numerical value.
 */
public class NumConst<V> extends Expr<V> {
    private final String value;

    private NumConst(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("NUM_CONST"), properties, comment);
        Assert.isTrue(!value.equals(""));
        this.value = value;
        setReadOnly();
    }

    /**
     * factory method: create an AST instance of {@link NumConst}
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object representing the number constant in the AST
     */
    public static <V> NumConst<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NumConst<>(value, properties, comment);
    }

    /**
     * factory method: create an AST instance of {@link NumConst}.<br>
     * <b>Main use: either testing or textual representation of programs (because in this case no graphical regeneration is required.</b>
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @return read only object representing the number constant in the AST
     */
    public static <V> NumConst<V> make(String value) {
        return new NumConst<>(value, BlocklyBlockProperties.make("1", "1"), null);
    }

    /**
     * @return value of the numerical constant
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
        return BlocklyType.NUMBER;
    }

    @Override
    public String toString() {
        return "NumConst [" + this.value + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitNumConst(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String field = helper.extractField(fields, BlocklyConstants.NUM);
        return NumConst.make(field, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.NUM, getValue());
        return jaxbDestination;
    }
}
