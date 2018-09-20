package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

/**
 * This class represents the <b>mbedActions_single_motor_stop</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for turning off the motor.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotorStopMode} (is the motor breaking or not).
 */
public class BothMotorsStopAction<V> extends Action<V> {

    private BothMotorsStopAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOTH_MOTORS_STOP_ACTION"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link BothMotorsStopAction}. This instance is read only and can not be modified.
     *
     * @param mode of stopping {@link MotorStopMode}; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BothMotorsStopAction}
     */
    private static <V> BothMotorsStopAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BothMotorsStopAction<>(properties, comment);
    }

    @Override
    public String toString() {
        return "SingleMotorStopAction []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitBothMotorsStopAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        return BothMotorsStopAction.make(helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
