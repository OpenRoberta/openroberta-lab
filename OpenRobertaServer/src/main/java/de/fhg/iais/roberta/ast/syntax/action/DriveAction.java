package de.fhg.iais.roberta.ast.syntax.action;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode.<br/>
 * <br>
 * The client must provide the {@link DriveDirection} and {@link MotionParam} (distance the robot should cover and speed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(DriveDirection, MotionParam)}.<br>
 */
public class DriveAction<V> extends Action<V> {

    private final DriveDirection direction;
    private final MotionParam<V> param;

    private DriveAction(DriveDirection direction, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.DRIVE_ACTION, properties, comment);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DriveAction}. This instance is read only and can not be modified.
     *
     * @param direction {@link DriveDirection} in which the robot will drive; must be <b>not</b> null,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed), must be <b>not</b>
     *        null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DriveAction}
     */
    private static <V> DriveAction<V> make(DriveDirection direction, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DriveAction<V>(direction, param, properties, comment);
    }

    /**
     * @return {@link DriveDirection} in which the robot will drive
     */
    public DriveDirection getDirection() {
        return this.direction;
    }

    /**
     * @return {@link MotionParam} for the motor (speed and distance in which the motors are set).
     */
    public MotionParam<V> getParam() {
        return this.param;
    }

    @Override
    public String toString() {
        return "DriveAction [" + this.direction + ", " + this.param + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitDriveAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam<V> mp;
        Phrase<V> left;

        fields = helper.extractFields(block, (short) 1);
        mode = helper.extractField(fields, BlocklyConstants.DIRECTION);

        if ( !block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON) ) {
            values = helper.extractValues(block, (short) 2);
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
            Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.DISTANCE, Integer.class));
            MotorDuration<V> md = new MotorDuration<V>(MotorMoveMode.DISTANCE, helper.convertPhraseToExpr(right));
            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(left)).duration(md).build();
        } else {
            values = helper.extractValues(block, (short) 1);
            left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(left)).build();
        }
        return DriveAction.make(DriveDirection.get(mode), mp, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER, getParam().getSpeed());

        if ( getParam().getDuration() != null ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, getParam().getDuration().getType().name(), getParam().getDuration().getValue());
        }
        return jaxbDestination;
    }
}
