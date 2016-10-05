package de.fhg.iais.roberta.syntax.action.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robActions_brickLight_off</b> and <b>robActions_brickLight_reset</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning the light off or reset them.<br/>
 * <br/>
 * The client must provide the {@link Status}.
 */
public class LightStatusAction<V> extends Action<V> {
    private final Status status;

    private LightStatusAction(Status status, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIGHT_STATUS_ACTION"),properties, comment);
        Assert.isTrue(status != null);
        this.status = status;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightStatusAction}. This instance is read only and can not be modified.
     *
     * @param status in which we want to set the lights (off or reset); must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LightStatusAction}
     */
    public static <V> LightStatusAction<V> make(Status status, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LightStatusAction<V>(status, properties, comment);
    }

    /**
     * @return status of the lights user wants to set.
     */
    public Status getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "LightStatusAction [" + this.status + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitLightStatusAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        Status statuss = LightStatusAction.Status.RESET;
        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_OFF) ) {
            statuss = LightStatusAction.Status.OFF;
        }
        return LightStatusAction.make(statuss, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

    /**
     * Status in which user can set the lights.
     */
    public static enum Status {
        OFF, RESET;
    }
}
