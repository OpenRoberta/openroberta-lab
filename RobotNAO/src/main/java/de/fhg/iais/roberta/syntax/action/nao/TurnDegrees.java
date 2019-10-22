package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.TurnDirection;
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
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>naoActions_turn</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting .<br/>
 * <br/>
 * The client must provide the {@link turnDirection} and {@link degreesToTurn} (direction and number of degrees to turn).
 */
public final class TurnDegrees<V> extends Action<V> {

    private final TurnDirection turnDirection;
    private final Expr<V> degreesToTurn;

    private TurnDegrees(TurnDirection turnDirection, Expr<V> degreesToTurn, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TURN_DEGREES"), properties, comment);
        Assert.notNull(turnDirection, "Missing degrees in TurnDegrees block!");
        this.turnDirection = turnDirection;
        this.degreesToTurn = degreesToTurn;
        setReadOnly();
    }

    /**
     * Creates instance of {@link TurnDegrees}. This instance is read only and can not be modified.
     *
     * @param direction {@link turnDirection} the robot will turn to,
     * @param degrees {@link degreesToTurn} the robot will turn (radians),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link TurnDegrees}
     */
    private static <V> TurnDegrees<V> make(TurnDirection turnDirection, Expr<V> degreesToTurn, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TurnDegrees<V>(turnDirection, degreesToTurn, properties, comment);
    }

    public TurnDirection getTurnDirection() {
        return this.turnDirection;
    }

    public Expr<V> getDegreesToTurn() {
        return this.degreesToTurn;
    }

    @Override
    public String toString() {
        return "TurnDegrees [" + this.turnDirection + ", " + this.degreesToTurn + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitTurnDegrees(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        List<Value> values = helper.extractValues(block, (short) 1);

        String turnDirection = helper.extractField(fields, BlocklyConstants.DIRECTION);
        Phrase<V> walkDistance = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));

        return TurnDegrees
            .make(
                TurnDirection.get(turnDirection),
                helper.convertPhraseToExpr(walkDistance),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, this.turnDirection.toString());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.POWER, this.degreesToTurn);

        return jaxbDestination;
    }
}
