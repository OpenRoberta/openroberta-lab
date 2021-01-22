package de.fhg.iais.roberta.syntax.actors.raspberrypi;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IMotorServoMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from
 * this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link String} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class ServoRaspOnAction<V> extends MoveAction<V> {
    private final IMotorServoMode mode;

    private ServoRaspOnAction(String port, IMotorServoMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("MOTOR_SERVO_ACTION_RASP"), properties, comment);
        Assert.isTrue((mode != null) && (port != null));
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ServoRaspOnAction}. This instance is read only and can not be modified.
     *
     * @param port {@link String} on which the motor is connected,
     * @param mode {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ServoRaspOnAction}
     */
    public static <V> ServoRaspOnAction<V> make(String port, IMotorServoMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ServoRaspOnAction<>(port, mode, properties, comment);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {

        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.POSITION);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.MOTORPORT);

        return ServoRaspOnAction.make(port, factory.getMotorServoMode(mode), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    public IMotorServoMode getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "MotorOnAction [" + getUserDefinedPort() + ", " + this.mode + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.POSITION, getMode().toString());
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getUserDefinedPort());
        return jaxbDestination;
    }
}
