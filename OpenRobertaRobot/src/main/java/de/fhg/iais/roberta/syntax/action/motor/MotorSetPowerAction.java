package de.fhg.iais.roberta.syntax.action.motor;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;

/**
 * This class represents the <b>robActions_motor_setPower</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for setting the power of the motor on given port.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} on which the motor is connected.
 */
public class MotorSetPowerAction<V> extends MoveAction<V> {
    private final Expr<V> power;

    private MotorSetPowerAction(String port, Expr<V> power, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("MOTOR_SET_POWER_ACTION"), properties, comment);
        Assert.isTrue(port != null && power.isReadOnly());
        this.power = power;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorSetPowerAction}. This instance is read only and can not be modified.
     *
     * @param port on which the motor is connected that we want to set; must be <b>not</b> null,
     * @param power to which motor should be set; must be <b>read only</b>
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorSetPowerAction}
     */
    public static <V> MotorSetPowerAction<V> make(String port, Expr<V> power, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorSetPowerAction<V>(port, power, properties, comment);
    }

    /**
     * @return value of the power of the motor.
     */
    public Expr<V> getPower() {
        return this.power;
    }

    @Override
    public String toString() {
        return "MotorSetPowerAction [port=" + getUserDefinedPort() + ", power=" + this.power + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMotorVisitor<V>) visitor).visitMotorSetPowerAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        List<Value> values = helper.extractValues(block, (short) 1);
        String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
        Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
        return MotorSetPowerAction
            .make(factory.sanitizePort(portName), helper.convertPhraseToExpr(left), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getUserDefinedPort().toString());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.POWER, getPower());

        return jaxbDestination;
    }
}
