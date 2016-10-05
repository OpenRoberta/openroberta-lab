package de.fhg.iais.roberta.syntax.action.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robActions_display_clear</b> block from Blockly into the AST (abstract syntax tree).
 */
public final class ClearDisplayAction<V> extends Action<V> {

    private ClearDisplayAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("CLEAR_DISPLAY_ACTION"),properties, comment);
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
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        return ClearDisplayAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
