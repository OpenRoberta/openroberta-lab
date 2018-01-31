package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.Bob3AstVisitor;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class RecallAction<V> extends Action<V> {

    private RecallAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOB3_RECALL"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link RecallAction}. This instance is read only and can not be modified.
     */
    private static <V> RecallAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RecallAction<>(properties, comment);
    }

    @Override
    public String toString() {
        return "RecallAction [ ]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((Bob3AstVisitor<V>) visitor).visitRecallAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        return RecallAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;

    }
}
