package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This class represents the <b>mbedActions_single_motor_on</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class BothMotorsOnAction<V> extends Action<V> {
    private final Expr<V> speedA;
    private final Expr<V> speedB;
    private final String portA;
    private final String portB;

    private BothMotorsOnAction(String portA, String portB, Expr<V> speedA, Expr<V> speedB, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOTH_MOTORS_ON_ACTION"), properties, comment);
        Assert.isTrue((speedA != null) && speedA.isReadOnly());
        Assert.isTrue((speedB != null) && speedB.isReadOnly());
        this.portA = portA;
        this.portB = portB;
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
    public static <V> BothMotorsOnAction<V> make(
        String portA,
        String portB,
        Expr<V> speedA,
        Expr<V> speedB,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new BothMotorsOnAction<>(portA, portB, speedA, speedB, properties, comment);
    }

    public Expr<V> getSpeedA() {
        return this.speedA;
    }

    public Expr<V> getSpeedB() {
        return this.speedB;
    }

    public String getPortA() {
        return this.portA;
    }

    public String getPortB() {
        return this.portB;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {

        List<Value> values = helper.extractValues(block, (short) 2);
        List<Field> fields = helper.extractFields(block, (short) 2);
        Phrase<V> speedA = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_A, BlocklyType.NUMBER_INT));
        Phrase<V> speedB = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER_B, BlocklyType.NUMBER_INT));
        String portA = helper.extractField(fields, BlocklyConstants.A, BlocklyConstants.A);
        String portB = helper.extractField(fields, BlocklyConstants.B, BlocklyConstants.B);

        return BothMotorsOnAction
            .make(
                portA,
                portB,
                helper.convertPhraseToExpr(speedA),
                helper.convertPhraseToExpr(speedB),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public String toString() {
        return "BothMotorsOnAction [" + this.portA + ", " + this.speedA + ", " + this.portB + ", " + this.speedB + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitBothMotorsOnAction(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.POWER_A, this.speedA);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.POWER_B, this.speedB);
        if ( !this.portA.toString().equals(BlocklyConstants.A) ) {
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.A, this.portA);
        }
        if ( !this.portB.toString().equals(BlocklyConstants.B) ) {
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.B, this.portB);
        }

        return jaxbDestination;
    }
}
