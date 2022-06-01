package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Frame;
import de.fhg.iais.roberta.mode.action.nao.PointLook;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * This class represents the <b>naoActions_pointAt</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * making the NAO point at a specific position.<br/>
 * <br/>
 * The client must provide the {@link Frame}, {@link pointX}, {@link pointY}, {@link pointZ} and {@link speed} (frame, coordinates and fraction of speed).
 */
public final class PointLookAt<V> extends Action<V> {

    private final Frame frame;
    private final PointLook pointLook;
    private final Expr<V> pointX;
    private final Expr<V> pointY;
    private final Expr<V> pointZ;
    private final Expr<V> speed;

    private PointLookAt(
        Frame frame,
        PointLook pointLook,
        Expr<V> pointX,
        Expr<V> pointY,
        Expr<V> pointZ,
        Expr<V> speed,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("POINT_LOOK_AT"), properties, comment);
        //Assert.notNull(frame, "Missing frame in PointLookAt block!");
        //Assert.notNull(pointLook, "Missing point look in PointLookAt block!");
        this.frame = frame;
        this.pointLook = pointLook;
        this.pointX = pointX;
        this.pointY = pointY;
        this.pointZ = pointZ;
        this.speed = speed;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PointLookAt}. This instance is read only and can not be modified.
     *
     * @param frame {@link Frame} the coordinates relate to,
     * @param X {@link lookX} x coordinate,
     * @param Y {@link lookY} y coordinate,
     * @param Z {@link lookZ} z coordinate,
     * @param speed {@link speed} the movement will be executed at,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PointLookAt}
     */
    private static <V> PointLookAt<V> make(
        Frame frame,
        PointLook pointLook,
        Expr<V> pointX,
        Expr<V> pointY,
        Expr<V> pointZ,
        Expr<V> speed,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new PointLookAt<V>(frame, pointLook, pointX, pointY, pointZ, speed, properties, comment);
    }

    public Frame getFrame() {
        return this.frame;
    }

    public PointLook getPointLook() {
        return this.pointLook;
    }

    public Expr<V> getPointX() {
        return this.pointX;
    }

    public Expr<V> getPointY() {
        return this.pointY;
    }

    public Expr<V> getPointZ() {
        return this.pointZ;
    }

    public Expr<V> getSpeed() {
        return this.speed;
    }

    @Override
    public String toString() {
        return "PointLookAt [" + this.frame + ", " + this.pointLook + ", " + this.pointX + ", " + this.pointY + ", " + this.pointZ + ", " + this.speed + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 4);

        String pointLook = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
        String frame = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);
        Phrase<V> pointX = helper.extractValue(values, new ExprParam(BlocklyConstants.X, BlocklyType.NUMBER_INT));
        Phrase<V> pointY = helper.extractValue(values, new ExprParam(BlocklyConstants.Y, BlocklyType.NUMBER_INT));
        Phrase<V> pointZ = helper.extractValue(values, new ExprParam(BlocklyConstants.Z, BlocklyType.NUMBER_INT));
        Phrase<V> speed = helper.extractValue(values, new ExprParam(BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));

        return PointLookAt
            .make(
                Frame.get(frame),
                PointLook.get(pointLook),
                Jaxb2Ast.convertPhraseToExpr(pointX),
                Jaxb2Ast.convertPhraseToExpr(pointY),
                Jaxb2Ast.convertPhraseToExpr(pointZ),
                Jaxb2Ast.convertPhraseToExpr(speed),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.pointLook.getValues()[0]);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.frame.getValues()[0]);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.X, this.pointX);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Y, this.pointY);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Z, this.pointZ);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.SPEED, this.speed);

        return jaxbDestination;
    }
}
