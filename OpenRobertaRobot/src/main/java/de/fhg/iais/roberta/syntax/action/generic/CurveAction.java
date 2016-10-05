package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode.<br/>
 * <br>
 * The client must provide the {@link DriveDirection} and {@link MotionParam} (distance the robot should cover and speed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(DriveDirection, MotionParam)}.<br>
 */
public class CurveAction<V> extends Action<V> {

    private final IDriveDirection direction;
    private MotionParam<V> paramLeft;
    private MotionParam<V> paramRight;

    private CurveAction(
        IDriveDirection direction,
        MotionParam<V> paramLeft,
        MotionParam<V> paramRight,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("CURVE_ACTION"),properties, comment);
        Assert.isTrue(direction != null && paramLeft != null && paramRight != null);
        this.direction = direction;
        this.paramLeft = paramLeft;
        this.paramRight = paramRight;
        setReadOnly();
    }

    /**
     * Creates instance of {@link CurveAction}. This instance is read only and can not be modified.
     *
     * @param direction {@link DriveDirection} in which the robot will drive; must be <b>not</b> null,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed), must be <b>not</b>
     *        null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link CurveAction}
     */
    private static <V> CurveAction<V> make(
        IDriveDirection direction,
        MotionParam<V> paramLeft,
        MotionParam<V> paramRight,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new CurveAction<>(direction, paramLeft, paramRight, properties, comment);
    }

    /**
     * @return {@link DriveDirection} in which the robot will drive
     */
    public IDriveDirection getDirection() {
        return this.direction;
    }

    /**
     * @return {@link MotionParam} for the motor (speed and distance in which the motors are set).
     */
    public MotionParam<V> getParamLeft() {
        return this.paramLeft;
    }

    public MotionParam<V> getParamRight() {
        return this.paramRight;
    }

    @Override
    public String toString() {
        return "CurveAction [" + this.direction + ", " + this.paramLeft + this.paramRight + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitCurveAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam<V> mpLeft;
        MotionParam<V> mpRight;
        Phrase<V> left;
        Phrase<V> right;
        IRobotFactory factory = helper.getModeFactory();
        fields = helper.extractFields(block, (short) 1);
        mode = helper.extractField(fields, BlocklyConstants.DIRECTION);

        if ( !block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE) ) {
            values = helper.extractValues(block, (short) 3);
            Phrase<V> dist = helper.extractValue(values, new ExprParam(BlocklyConstants.DISTANCE, Integer.class));
            MotorDuration<V> md = new MotorDuration<>(factory.getMotorMoveMode("DISTANCE"), helper.convertPhraseToExpr(dist));
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_LEFT, Integer.class));
            right = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_RIGHT, Integer.class));
            mpLeft = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(left)).duration(md).build();
            mpRight = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(right)).duration(md).build();
        } else {
            values = helper.extractValues(block, (short) 2);
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_LEFT, Integer.class));
            right = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_RIGHT, Integer.class));
            mpLeft = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(left)).build();
            mpRight = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(right)).build();
        }
        return CurveAction.make(factory.getDriveDirection(mode), mpLeft, mpRight, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        if ( getProperty().getBlockType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE_FOR) ) {
            JaxbTransformerHelper
                .addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString() == "FOREWARD" ? getDirection().toString() : "BACKWARDS");
        } else {
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString());
        }
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER_LEFT, getParamLeft().getSpeed());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER_RIGHT, getParamRight().getSpeed());

        if ( getParamLeft().getDuration() != null ) {
            JaxbTransformerHelper.addValue(jaxbDestination, getParamLeft().getDuration().getType().toString(), getParamLeft().getDuration().getValue());
        }
        return jaxbDestination;
    }
}
