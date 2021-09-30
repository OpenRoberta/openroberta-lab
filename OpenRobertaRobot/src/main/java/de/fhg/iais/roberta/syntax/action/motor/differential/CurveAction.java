package de.fhg.iais.roberta.syntax.action.motor.differential;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from
 * this class will generate code for setting the motors in pilot mode.<br/>
 * <br>
 * The client must provide the {@link DriveDirection} and {@link MotionParam} (distance the robot should cover and speed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(DriveDirection, MotionParam)}.<br>
 */
public class CurveAction<V> extends Action<V> {

    private final IDriveDirection direction;
    private final MotionParam<V> paramLeft;
    private final MotionParam<V> paramRight;
    private final String port;
    private final List<Hide> hide;

    private CurveAction(
        IDriveDirection direction,
        MotionParam<V> paramLeft,
        MotionParam<V> paramRight,
        String port,
        List<Hide> hide,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("CURVE_ACTION"), properties, comment);
        Assert.isTrue(direction != null && paramLeft != null && paramRight != null);
        this.direction = direction;
        this.paramLeft = paramLeft;
        this.paramRight = paramRight;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    /**
     * Creates instance of {@link CurveAction}. This instance is read only and can not be modified.
     *
     * @param direction {@link DriveDirection} in which the robot will drive; must be <b>not</b> null,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed), must be <b>not</b>
     *     null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link CurveAction}
     */
    public static <V> CurveAction<V> make(
        IDriveDirection direction,
        MotionParam<V> paramLeft,
        MotionParam<V> paramRight,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new CurveAction<>(direction, paramLeft, paramRight, BlocklyConstants.EMPTY_PORT, null, properties, comment);
    }

    public static <V> CurveAction<V> make(
        IDriveDirection direction,
        MotionParam<V> paramLeft,
        MotionParam<V> paramRight,
        String port,
        List<Hide> hide,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new CurveAction<>(direction, paramLeft, paramRight, port, hide, properties, comment);
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

    /**
     * @return {@link String} port the used actor is connected to
     */
    public String getPort() {
        return this.port;
    }

    /**
     * @return {@link List<Hide>} List of hidden ports
     */
    public List<Hide> getHide() {
        return this.hide;
    }

    @Override
    public String toString() {
        return "CurveAction [" + this.direction + ", " + this.paramLeft + this.paramRight + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam<V> mpLeft;
        MotionParam<V> mpRight;
        Phrase<V> left;
        Phrase<V> right;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        fields = Jaxb2Ast.extractFields(block, (short) 2);
        mode = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);

        if ( !block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE) ) {
            values = Jaxb2Ast.extractValues(block, (short) 3);
            Phrase<V> dist = helper.extractValue(values, new ExprParam(BlocklyConstants.DISTANCE, BlocklyType.NUMBER_INT));
            MotorDuration<V> md = new MotorDuration<>(factory.getMotorMoveMode("DISTANCE"), Jaxb2Ast.convertPhraseToExpr(dist));
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_LEFT, BlocklyType.NUMBER_INT));
            right = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_RIGHT, BlocklyType.NUMBER_INT));
            mpLeft = new MotionParam.Builder<V>().speed(Jaxb2Ast.convertPhraseToExpr(left)).duration(md).build();
            mpRight = new MotionParam.Builder<V>().speed(Jaxb2Ast.convertPhraseToExpr(right)).duration(md).build();
        } else {
            values = Jaxb2Ast.extractValues(block, (short) 2);
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_LEFT, BlocklyType.NUMBER_INT));
            right = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_RIGHT, BlocklyType.NUMBER_INT));
            mpLeft = new MotionParam.Builder<V>().speed(Jaxb2Ast.convertPhraseToExpr(left)).build();
            mpRight = new MotionParam.Builder<V>().speed(Jaxb2Ast.convertPhraseToExpr(right)).build();
        }

        if ( fields.stream().anyMatch(field -> field.getName().equals(BlocklyConstants.ACTORPORT)) ) {
            String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
            return CurveAction.make(factory.getDriveDirection(mode), mpLeft, mpRight, port, block.getHide(), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        }

        return CurveAction.make(factory.getDriveDirection(mode), mpLeft, mpRight, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        if ( getProperty().getBlockType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE_FOR) ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString() == "FOREWARD" ? getDirection().toString() : "BACKWARDS");
        } else {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString());
        }
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER_LEFT, getParamLeft().getSpeed());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER_RIGHT, getParamRight().getSpeed());

        if ( getParamLeft().getDuration() != null ) {
            Ast2Jaxb.addValue(jaxbDestination, getParamLeft().getDuration().getType().toString(), getParamLeft().getDuration().getValue());
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        if ( this.hide != null ) {
            jaxbDestination.getHide().addAll(hide);
        }
        return jaxbDestination;
    }
}
