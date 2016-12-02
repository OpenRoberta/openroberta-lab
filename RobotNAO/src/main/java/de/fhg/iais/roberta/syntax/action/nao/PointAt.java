package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.ActorPort;
import de.fhg.iais.roberta.mode.action.nao.Frame;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class PointAt<V> extends Action<V> {

    private final Frame frame;
    private final Expr<V> pointX;
    private final Expr<V> pointY;
    private final Expr<V> pointZ;
    private final Expr<V> speed;

    private PointAt(Frame frame, Expr<V> pointX, Expr<V> pointY, Expr<V> pointZ, Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("POINT_AT"), properties, comment);
        Assert.notNull(frame, "Missing frame in PointAt block!");
        this.frame = frame;
        this.pointX = pointX;
        this.pointY = pointY;
        this.pointZ = pointZ;
        this.speed = speed;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PointAt}. This instance is read only and can not be modified.
     *
     * @param port {@link ActorPort} on which the motor is connected,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PointAt}
     */
    private static <V> PointAt<V> make(
        Frame frame,
        Expr<V> pointX,
        Expr<V> pointY,
        Expr<V> pointZ,
        Expr<V> speed,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new PointAt<V>(frame, pointX, pointY, pointZ, speed, properties, comment);
    }

    public Frame getFrame() {
        return this.frame;
    }

    public Expr<V> getpointX() {
        return this.pointX;
    }

    public Expr<V> getpointY() {
        return this.pointY;
    }

    public Expr<V> getpointZ() {
        return this.pointZ;
    }

    public Expr<V> getSpeed() {
        return this.speed;
    }

    @Override
    public String toString() {
        return "PointAt [" + this.frame + ", " + this.pointX + ", " + this.pointY + ", " + this.pointZ + ", " + this.speed + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitPointAt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        List<Value> values = helper.extractValues(block, (short) 4);

        String frame = helper.extractField(fields, BlocklyConstants.DIRECTION);
        Phrase<V> pointX = helper.extractValue(values, new ExprParam(BlocklyConstants.X, Integer.class));
        Phrase<V> pointY = helper.extractValue(values, new ExprParam(BlocklyConstants.Y, Integer.class));
        Phrase<V> pointZ = helper.extractValue(values, new ExprParam(BlocklyConstants.Z, Integer.class));
        Phrase<V> speed = helper.extractValue(values, new ExprParam(BlocklyConstants.Speed, Integer.class));

        return PointAt.make(
            Frame.get(frame),
            helper.convertPhraseToExpr(pointX),
            helper.convertPhraseToExpr(pointY),
            helper.convertPhraseToExpr(pointZ),
            helper.convertPhraseToExpr(speed),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.frame.toString());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.X, this.pointX);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.Y, this.pointY);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.Z, this.pointZ);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.Speed, this.speed);

        return jaxbDestination;
    }
}
