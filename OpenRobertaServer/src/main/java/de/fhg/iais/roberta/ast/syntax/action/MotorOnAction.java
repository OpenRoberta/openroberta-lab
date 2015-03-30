package de.fhg.iais.roberta.ast.syntax.action;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
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
 * Object from this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class MotorOnAction<V> extends Action<V> {
    private final ActorPort port;
    private final MotionParam<V> param;

    private MotorOnAction(ActorPort port, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.MOTOR_ON_ACTION, properties, comment);
        Assert.isTrue(param != null && port != null);
        this.param = param;
        this.port = port;
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
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        String port;
        List<Field> fields;
        List<Value> values;
        MotionParam<V> mp;

        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_ON) ) {
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
     * @return port on which the motor is connected.
     */
    public ActorPort getPort() {
        return this.port;
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
        return this.param.getDuration().getValue();
    }

    @Override
    public String toString() {
        return "MotorOnAction [" + this.port + ", " + this.param + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMotorOnAction(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getPort().name());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER, getParam().getSpeed());

        if ( getParam().getDuration() != null ) {
            AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MOTORROTATION, getDurationMode().name());
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, getDurationValue());
        }

        return jaxbDestination;
    }
}
