package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.DriveDirection;
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
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for making
 * the robot walk for a distance.<br/>
 * <br/>
 * The client must provide the {@link walkDirection} and {@link distanceToWalk} (direction and distance to walk).
 */
public final class WalkDistance<V> extends Action<V> {

    private final DriveDirection walkDirection;
    private final Expr<V> distanceToWalk;

    private WalkDistance(DriveDirection walkDirection, Expr<V> distanceToWalk, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WALK_DISTANCE"), properties, comment);
        Assert.notNull(walkDirection, "Missing direction in WalkDistance block!");
        this.walkDirection = walkDirection;
        this.distanceToWalk = distanceToWalk;
        setReadOnly();
    }

    /**
     * Creates instance of {@link WalkDistance}. This instance is read only and can not be modified.
     *
     * @param direction {@link walkDirection} the robot will walk,
     * @param distance {@link distanceToWalk} the robot will walk for,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link WalkDistance}
     */
    private static <V> WalkDistance<V> make(DriveDirection walkDirection, Expr<V> distanceToWalk, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new WalkDistance<V>(walkDirection, distanceToWalk, properties, comment);
    }

    public DriveDirection getWalkDirection() {
        return this.walkDirection;
    }

    public Expr<V> getDistanceToWalk() {
        return this.distanceToWalk;
    }

    @Override
    public String toString() {
        return "WalkDistance [" + this.walkDirection + ", " + this.distanceToWalk + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        String walkDirection = Jaxb2Ast.extractField(fields, BlocklyConstants.DIRECTION);
        Phrase<V> walkDistance = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));

        return WalkDistance
            .make(
                DriveDirection.get(walkDirection),
                Jaxb2Ast.convertPhraseToExpr(walkDistance),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.walkDirection.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, this.distanceToWalk);

        return jaxbDestination;
    }
}
