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
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>text</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for string
 * constant.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class StringConst<V> extends Expr<V> {
    private final String value;

    private StringConst(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("STRING_CONST"), properties, comment);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link StringConst}. This instance is read only and can not be modified.
     *
     * @param value that the boolean constant will have,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StringConst}
     */
    public static <V> StringConst<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StringConst<V>(value, properties, comment);
    }

    /**
     * @return the value of the string constant
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
        return BlocklyType.STRING;
    }

    @Override
    public String toString() {
        return "StringConst [" + this.value + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitStringConst(this);
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
        String field = helper.extractField(fields, BlocklyConstants.TEXT);
        return StringConst.make(field, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TEXT, getValue());
        return jaxbDestination;
    }
}
