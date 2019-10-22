package de.fhg.iais.roberta.syntax.action.motor.differential;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;

/**
 * This class represents the <b>robActions_motorDiff_stop</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * to stop the work of the motors.<br/>
 */
public class MotorDriveStopAction<V> extends Action<V> {

    private MotorDriveStopAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("STOP_ACTION"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorDriveStopAction}
     */
    public static <V> MotorDriveStopAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorDriveStopAction<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "StopAction []";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IDifferentialMotorVisitor<V>) visitor).visitMotorDriveStopAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return MotorDriveStopAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
