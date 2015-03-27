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
 * This class represents the <b>robActions_motorDiff_turn</b> and <b>robActions_motorDiff_turn_for</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode and turn on given direction.<br/>
 * <br/>
 * The client must provide the {@link TurnDirection} and {@link MotionParam} (distance the robot should cover and speed).
 */
public class TurnAction<V> extends Action<V> {
    private final TurnDirection direction;
    private final MotionParam<V> param;

    private TurnAction(TurnDirection direction, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.TURN_ACTION, properties, comment);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link TurnAction}. This instance is read only and can not be modified.
     *
     * @param direction {@link TurnDirection} in which the robot will drive; must be <b>not</b> null,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed); must be <b>not</b>
     *        null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link TurnAction}.
     */
    private static <V> TurnAction<V> make(TurnDirection direction, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TurnAction<V>(direction, param, properties, comment);
    }

    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam<V> mp;
        if ( block.getType().equals("robActions_motorDiff_turn") ) {
            fields = helper.extractFields(block, (short) 1);
            mode = helper.extractField(fields, BlocklyConstants.DIRECTION);
            values = helper.extractValues(block, (short) 1);
            Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(expr)).build();
            return TurnAction.make(TurnDirection.get(mode), mp, helper.extractBlockProperties(block), helper.extractComment(block));
        }
        fields = helper.extractFields(block, (short) 1);
        mode = helper.extractField(fields, BlocklyConstants.DIRECTION);
        values = helper.extractValues(block, (short) 2);
        Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, Integer.class));
        Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.DEGREE, Integer.class));
        MotorDuration<V> md = new MotorDuration<V>(MotorMoveMode.DEGREE, helper.convertPhraseToExpr(right));
        mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(left)).duration(md).build();
        return TurnAction.make(TurnDirection.get(mode), mp, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    /**
     * @return {@link TurnDirection} in which the robot will drive
     */
    public TurnDirection getDirection() {
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
        return "TurnAction [direction=" + this.direction + ", param=" + this.param + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitTurnAction(this);
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
