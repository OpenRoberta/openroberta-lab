package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
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
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for .<br/>
 * <br/>
 * The client must provide the {@link walkToX}, {@link walkToY} and {@link walkToTheta} (coordinates the robot will walk to).
 */
public final class WalkTo<V> extends Action<V> {

    private final Expr<V> walkToX;
    private final Expr<V> walkToY;
    private final Expr<V> walkToTheta;

    private WalkTo(Expr<V> walkToX, Expr<V> walkToY, Expr<V> walkToTheta, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WALK_DISTANCE"), properties, comment);
        this.walkToX = walkToX;
        this.walkToY = walkToY;
        this.walkToTheta = walkToTheta;
        setReadOnly();
    }

    /**
     * Creates instance of {@link WalkTo}. This instance is read only and can not be modified.
     *
     * @param X {@link walkToX} x coordinate,
     * @param Y {@link walkToY} y coordinate,
     * @param theta {@link walkToTheta} theta coordinate,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link WalkTo}
     */
    private static <V> WalkTo<V> make(Expr<V> walkToX, Expr<V> walkToY, Expr<V> walkToTheta, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new WalkTo<V>(walkToX, walkToY, walkToTheta, properties, comment);
    }

    public Expr<V> getWalkToX() {
        return this.walkToX;
    }

    public Expr<V> getWalkToY() {
        return this.walkToY;
    }

    public Expr<V> getWalkToTheta() {
        return this.walkToTheta;
    }

    @Override
    public String toString() {
        return "WalkTo [" + this.walkToX + ", " + this.walkToY + ", " + this.walkToTheta + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 3);

        Phrase<V> walkToX = helper.extractValue(values, new ExprParam(BlocklyConstants.X, BlocklyType.NUMBER_INT));
        Phrase<V> walkToY = helper.extractValue(values, new ExprParam(BlocklyConstants.Y, BlocklyType.NUMBER_INT));
        Phrase<V> walkToTheta = helper.extractValue(values, new ExprParam(BlocklyConstants.THETA, BlocklyType.NUMBER_INT));

        return WalkTo
            .make(
                Jaxb2Ast.convertPhraseToExpr(walkToX),
                Jaxb2Ast.convertPhraseToExpr(walkToY),
                Jaxb2Ast.convertPhraseToExpr(walkToTheta),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.X, this.walkToX);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Y, this.walkToY);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.THETA, this.walkToTheta);

        return jaxbDestination;
    }
}
