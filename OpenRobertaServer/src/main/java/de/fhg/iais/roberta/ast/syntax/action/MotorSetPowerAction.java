package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_setPower</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the power of the motor on given port.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} on which the motor is connected.
 */
public class MotorSetPowerAction extends Action {
    private final ActorPort port;
    private final Expr power;

    private MotorSetPowerAction(ActorPort port, Expr power) {
        super(Phrase.Kind.MOTOR_GET_POWER_ACTION);
        Assert.isTrue(port != null && power.isReadOnly());
        this.port = port;
        this.power = power;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorSetPowerAction}. This instance is read only and can not be modified.
     * 
     * @param port on which the motor is connected that we want to set,
     * @return read only object of class {@link MotorSetPowerAction}.
     */
    public static MotorSetPowerAction make(ActorPort port, Expr power) {
        return new MotorSetPowerAction(port, power);
    }

    /**
     * @return the port number on which the motor is connected.
     */
    public ActorPort getPort() {
        return this.port;
    }

    public Expr getPower() {
        return this.power;
    }

    @Override
    public String toString() {
        return "MotorSetPowerAction [port=" + this.port + ", power=" + this.power + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
