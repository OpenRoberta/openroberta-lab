package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_getPower</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for returning the power of the motor on given port.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} on which the motor is connected.
 */
public class MotorGetPowerAction<V> extends Action<V> {
    private final ActorPort port;

    private MotorGetPowerAction(ActorPort port, boolean disabled, String comment) {
        super(Phrase.Kind.MOTOR_GET_POWER_ACTION, disabled, comment);
        Assert.isTrue(port != null);
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorGetPowerAction}. This instance is read only and can not be modified.
     * 
     * @param port on which the motor is connected that we want to check,
     * @param disabled state of the block,
     * @param comment added from the user
     * @return read only object of class {@link MotorGetPowerAction}.
     */
    public static <V> MotorGetPowerAction<V> make(ActorPort port, boolean disabled, String comment) {
        return new MotorGetPowerAction<V>(port, disabled, comment);
    }

    /**
     * @return the port number on which the motor is connected.
     */
    public ActorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "MotorGetPower [port=" + this.port + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMotorGetPowerAction(this);
    }
}
