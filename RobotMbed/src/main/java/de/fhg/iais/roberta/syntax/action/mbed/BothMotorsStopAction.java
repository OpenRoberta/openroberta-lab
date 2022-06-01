package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

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
    public static <V> BothMotorsStopAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BothMotorsStopAction<>(properties, comment);
    }

    @Override
    public String toString() {
        return "SingleMotorStopAction []";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        return BothMotorsStopAction.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
