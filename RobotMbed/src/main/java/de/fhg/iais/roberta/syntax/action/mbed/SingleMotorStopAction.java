package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IMotorStopMode;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This class represents the <b>mbedActions_single_motor_stop</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for turning off the motor.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotorStopMode} (is the motor breaking or not).
 */
public class SingleMotorStopAction<V> extends Action<V> {
    private final IMotorStopMode mode;

    private SingleMotorStopAction(IMotorStopMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SINGLE_MOTOR_STOP_ACTION"), properties, comment);
        Assert.isTrue(mode != null);
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SingleMotorStopAction}. This instance is read only and can not be modified.
     *
     * @param mode of stopping {@link MotorStopMode}; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SingleMotorStopAction}
     */
    public static <V> SingleMotorStopAction<V> make(IMotorStopMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SingleMotorStopAction<>(mode, properties, comment);
    }

    /**
     * @return stopping mode in which the motor is set.
     */
    public IMotorStopMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "SingleMotorStopAction [" + this.mode + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitSingleMotorStopAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE);
        return SingleMotorStopAction.make(factory.getMotorStopMode(modeName), helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, getMode().toString());

        return jaxbDestination;
    }
}
