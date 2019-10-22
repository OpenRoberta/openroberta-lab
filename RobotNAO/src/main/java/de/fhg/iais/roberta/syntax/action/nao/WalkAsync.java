package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for .<br/>
 * <br/>
 * The client must provide the {@link walkToX}, {@link walkToY} and {@link walkToTheta} (coordinates the robot will walk to).
 */
public final class WalkAsync<V> extends Action<V> {

    private final Expr<V> XSpeed;
    private final Expr<V> YSpeed;
    private final Expr<V> ZSpeed;

    private WalkAsync(Expr<V> XSpeed, Expr<V> YSpeed, Expr<V> ZSpeed, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WALK_ASYNC"), properties, comment);
        this.XSpeed = XSpeed;
        this.YSpeed = YSpeed;
        this.ZSpeed = ZSpeed;
        setReadOnly();
    }

    /**
     * Creates instance of {@link WalkAsync}. This instance is read only and can not be modified.
     *
     * @param X {@link walkToX} x coordinate,
     * @param Y {@link walkToY} y coordinate,
     * @param theta {@link walkToTheta} theta coordinate,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link WalkAsync}
     */
    private static <V> WalkAsync<V> make(Expr<V> walkToX, Expr<V> walkToY, Expr<V> walkToTheta, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new WalkAsync<V>(walkToX, walkToY, walkToTheta, properties, comment);
    }

    public Expr<V> getXSpeed() {
        return this.XSpeed;
    }

    public Expr<V> getYSpeed() {
        return this.YSpeed;
    }

    public Expr<V> getZSpeed() {
        return this.ZSpeed;
    }

    @Override
    public String toString() {
        return "WalkTo [" + this.XSpeed + ", " + this.YSpeed + ", " + this.ZSpeed + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitWalkAsync(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 3);

        Phrase<V> XSpeed = helper.extractValue(values, new ExprParam(BlocklyConstants.X + BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));
        Phrase<V> YSpeed = helper.extractValue(values, new ExprParam(BlocklyConstants.Y + BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));
        Phrase<V> ZSpeed = helper.extractValue(values, new ExprParam(BlocklyConstants.Z + BlocklyConstants.SPEED, BlocklyType.NUMBER_INT));

        return WalkAsync
            .make(
                helper.convertPhraseToExpr(XSpeed),
                helper.convertPhraseToExpr(YSpeed),
                helper.convertPhraseToExpr(ZSpeed),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.X + BlocklyConstants.SPEED, this.XSpeed);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.Y + BlocklyConstants.SPEED, this.YSpeed);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.Z + BlocklyConstants.SPEED, this.ZSpeed);

        return jaxbDestination;
    }
}
