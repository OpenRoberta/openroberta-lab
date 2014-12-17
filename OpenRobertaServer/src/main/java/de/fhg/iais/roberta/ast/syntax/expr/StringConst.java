package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;

/**
 * This class represents the <b>text</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for string constant.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(String, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class StringConst<V> extends Expr<V> {
    private final String value;

    private StringConst(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.STRING_CONST, properties, comment);
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
    public String toString() {
        return "StringConst [" + this.value + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitStringConst(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addField(jaxbDestination, "TEXT", getValue());
        return jaxbDestination;
    }
}
