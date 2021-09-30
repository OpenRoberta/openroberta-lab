package de.fhg.iais.roberta.syntax.action.motor.differential;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
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
 * This class represents the <b>robActions_motorDiff_turn</b> and <b>robActions_motorDiff_turn_for</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode and turn on given direction.<br/>
 * <br/>
 * The client must provide the {@link TurnDirection} and {@link MotionParam} (distance the robot should cover and speed).
 */
public class TurnAction<V> extends Action<V> {
    private final ITurnDirection direction;
    private final MotionParam<V> param;
    private final String port;
    private final List<Hide> hide;

    private TurnAction(ITurnDirection direction, MotionParam<V> param, String port, List<Hide> hide, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TURN_ACTION"), properties, comment);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    /**
     * Creates instance of {@link TurnAction}. This instance is read only and can not be modified.
     *
     * @param direction {@link TurnDirection} in which the robot will drive; must be <b>not</b> null,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed); must be <b>not</b>
     *     null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link TurnAction}.
     */
    public static <V> TurnAction<V> make(ITurnDirection direction, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TurnAction<>(direction, param, BlocklyConstants.EMPTY_PORT, null, properties, comment);
    }

    public static <V> TurnAction<V> make(
        ITurnDirection direction,
        MotionParam<V> param,
        String port,
        List<Hide> hide,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new TurnAction<>(direction, param, port, hide, properties, comment);
    }

    /**
     * @return {@link TurnDirection} in which the robot will drive
     */
    public ITurnDirection getDirection() {
        return this.direction;
    }

    /**
     * @return {@link MotionParam} for the motor (speed and distance in which the motors are set).
     */
    public MotionParam<V> getParam() {
        return this.param;
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
        return "TurnAction [direction=" + this.direction + ", param=" + this.param + "]";
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
        MotionParam<V> mp;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        fields = Jaxb2Ast.extractFields(block, (short) 2);
        mode = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);

        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_TURN) ) {
            values = Jaxb2Ast.extractValues(block, (short) 1);
            Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            mp = new MotionParam.Builder<V>().speed(Jaxb2Ast.convertPhraseToExpr(expr)).build();
        } else {
            values = Jaxb2Ast.extractValues(block, (short) 2);
            Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.DEGREE, BlocklyType.NUMBER_INT));
            MotorDuration<V> md = new MotorDuration<V>(factory.getMotorMoveMode("DEGREE"), Jaxb2Ast.convertPhraseToExpr(right));
            mp = new MotionParam.Builder<V>().speed(Jaxb2Ast.convertPhraseToExpr(left)).duration(md).build();
        }

        if ( fields.stream().anyMatch(field -> field.getName().equals(BlocklyConstants.ACTORPORT)) ) {
            String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
            return TurnAction.make(factory.getTurnDirection(mode), mp, port, block.getHide(), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        }

        return TurnAction.make(factory.getTurnDirection(mode), mp, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, getParam().getSpeed());

        if ( getParam().getDuration() != null ) {
            Ast2Jaxb.addValue(jaxbDestination, getParam().getDuration().getType().toString(), getParam().getDuration().getValue());
        }

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, port);
        if ( this.hide != null ) {
            jaxbDestination.getHide().addAll(hide);
        }
        return jaxbDestination;
    }
}
