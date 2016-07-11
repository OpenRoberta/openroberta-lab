package de.fhg.iais.roberta.syntax.action.ev3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.action.ev3.MotorMoveMode;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class MotorOnAction<V> extends MoveAction<V> {

    private final MotionParam<V> param;

    private MotorOnAction(ActorPort port, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockType.MOTOR_ON_ACTION, properties, comment);
        Assert.isTrue(param != null && port != null);
        this.param = param;

        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorOnAction}. This instance is read only and can not be modified.
     *
     * @param port {@link ActorPort} on which the motor is connected,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorOnAction}
     */
    private static <V> MotorOnAction<V> make(ActorPort port, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorOnAction<V>(port, param, properties, comment);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        String port;
        List<Field> fields;
        List<Value> values;
        MotionParam<V> mp;

        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_ON) || block.getType().equals(BlocklyConstants.SIM_MOTOR_ON) ) {
            fields = helper.extractFields(block, (short) 1);
            values = helper.extractValues(block, (short) 1);
            port = helper.extractField(fields, BlocklyConstants.MOTORPORT);
            Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(expr)).build();
        } else {
            fields = helper.extractFields(block, (short) 2);
            values = helper.extractValues(block, (short) 2);
            port = helper.extractField(fields, BlocklyConstants.MOTORPORT);
            String mode = helper.extractField(fields, BlocklyConstants.MOTORROTATION);
            Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
            Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, Integer.class));
            MotorDuration<V> md = new MotorDuration<V>(MotorMoveMode.get(mode), helper.convertPhraseToExpr(right));
            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(left)).duration(md).build();
        }
        return MotorOnAction.make(ActorPort.get(port), mp, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    /**
     * @return {@link MotionParam} for the motor (number of rotations or degrees and speed).
     */
    public MotionParam<V> getParam() {
        return this.param;
    }

    /**
     * @return duration type of motor movement
     */
    public MotorMoveMode getDurationMode() {
        return this.param.getDuration().getType();
    }

    /**
     * @return value of the duration of the motor movement
     */
    public Expr<V> getDurationValue() {
        MotorDuration<V> duration = this.param.getDuration();
        if ( duration != null ) {
            return duration.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MotorOnAction [" + getPort() + ", " + this.param + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMotorOnAction(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getPort().name());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER, getParam().getSpeed());

        if ( getParam().getDuration() != null ) {
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MOTORROTATION, getDurationMode().name());
            JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, getDurationValue());
        }

        return jaxbDestination;
    }
}
