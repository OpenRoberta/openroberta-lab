package de.fhg.iais.roberta.syntax.action.motor;

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
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;

/**
 * This class represents the <b>robActions_motor_stop</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning off the motor.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotorStopMode} (is the motor breaking or not).
 */
public class MotorStopAction<V> extends MoveAction<V> {
    private final IMotorStopMode mode;

    private MotorStopAction(String port, IMotorStopMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("MOTOR_STOP_ACTION"), properties, comment);
        Assert.isTrue(port != null);
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorStopAction}. This instance is read only and can not be modified.
     *
     * @param port {@link ActorPort} on which the motor is connected; must be <b>not</b> null,
     * @param mode of stopping {@link MotorStopMode}; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorStopAction}
     */
    public static <V> MotorStopAction<V> make(String port, IMotorStopMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorStopAction<V>(port, mode, properties, comment);
    }

    /**
     * @return stopping mode in which the motor is set.
     */
    public IMotorStopMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        if ( getMode() != null ) {
            return "MotorStop [port=" + getUserDefinedPort() + ", mode=" + this.mode + "]";
        } else {
            return "MotorStop [port=" + getUserDefinedPort() + "]";
        }
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMotorVisitor<V>) visitor).visitMotorStopAction(this);
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
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
        if ( fields.size() > 1 ) {
            String modeName = helper.extractField(fields, BlocklyConstants.MODE);
            return MotorStopAction
                .make(factory.sanitizePort(portName), factory.getMotorStopMode(modeName), helper.extractBlockProperties(block), helper.extractComment(block));

        }
        return MotorStopAction.make(factory.sanitizePort(portName), null, helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getUserDefinedPort().toString());
        if ( getMode() != null ) {
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, getMode().toString());
        }

        return jaxbDestination;
    }
}
