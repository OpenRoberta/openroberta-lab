package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
public final class ClearDisplayAction<V> extends Action<V> {

    private ClearDisplayAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Kind.CLEAR_DISPLAY_ACTION, properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link ClearDisplayAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ClearDisplayAction}
     */
    public static <V> ClearDisplayAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ClearDisplayAction<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "ClearDisplayAction []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitClearDisplayAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        return ClearDisplayAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
