package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;

/**
 * This class represents the <b>lists_create_empty</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for creating <b>empty list</b>.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class EmptyList<V> extends Expr<V> {

    private EmptyList(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.EMPTY_LIST, properties, comment);
        setReadOnly();
    }

    /**
     * creates instance of {@link EmptyList}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NullConst}
     */
    public static <V> EmptyList<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EmptyList<V>(properties, comment);
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
        return "EmptyList []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitEmptyList(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

}
