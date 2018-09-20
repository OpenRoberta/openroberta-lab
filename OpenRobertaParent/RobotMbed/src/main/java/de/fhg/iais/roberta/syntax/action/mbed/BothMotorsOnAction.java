package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

/**
 * This class represents the <b>mbedActions_single_motor_on</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class BothMotorsOnAction<V> extends Action<V> {
    private final Expr<V> speedA;
    private final Expr<V> speedB;

    private BothMotorsOnAction(Expr<V> speedA, Expr<V> speedB, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOTH_MOTORS_ON_ACTION"), properties, comment);
        Assert.isTrue((speedA != null) && speedA.isReadOnly());
        Assert.isTrue((speedB != null) && speedB.isReadOnly());
        this.speedA = speedA;
        this.speedB = speedB;

        setReadOnly();
    }

    /**
     * Creates instance of {@link BothMotorsOnAction}. This instance is read only and can not be modified.
     *
     * @param speed {@link Expr} speed of the robot,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BothMotorsOnAction}
     */
    private static <V> BothMotorsOnAction<V> make(Expr<V> speedA, Expr<V> speedB, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BothMotorsOnAction<>(speedA, speedB, properties, comment);
    }

    public Expr<V> getSpeedA() {
        return this.speedA;
    }

    public Expr<V> getSpeedB() {
        return this.speedB;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {

        List<Value> values = helper.extractValues(block, (short) 2);
        Phrase<V> speedA = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_A, BlocklyType.NUMBER_INT));
        Phrase<V> speedB = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_B, BlocklyType.NUMBER_INT));

        return BothMotorsOnAction
            .make(helper.convertPhraseToExpr(speedA), helper.convertPhraseToExpr(speedB), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public String toString() {
        return "BothMotorsOnAction [" + this.speedA + ", " + this.speedB + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitBothMotorsOnAction(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER_A, this.speedA);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.POWER_B, this.speedB);

        return jaxbDestination;
    }
}
