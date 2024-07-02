package de.fhg.iais.roberta.util.syntax;

import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.util.visitor.IInfoCollectable;

/**
 * This class is a parameter class used to set the motor duration mode {@link MotorMoveMode} and a value for the duration.
 */
public class MotorDuration implements IInfoCollectable {
    public IMotorMoveMode type;
    public Expr value;

    /**
     * This constructor creates correct object of the class {@link MotorDuration}.<br>
     * <br>
     * Client must provide type of the motor movement (<i>see enum {@link MotorMoveMode} for all possible motor movements</i>) and value for the movement e.g.
     * distance(cm), number of rotations...
     *
     * @param type of the motor movement
     * @param value for the movement
     */
    public MotorDuration(IMotorMoveMode type, Expr value) {
        super();
        this.type = type;
        this.value = value;
    }

    /**
     * Returns the value of the duration set by the client.
     *
     * @return value of the duration as {@link Expr}
     */
    public Expr getValue() {
        return this.value;
    }

    /**
     * Get the mode of the motor duration. See enum {@link MotorMoveMode} for all possible modes.
     *
     * @return mode of the motor duration.
     */
    public IMotorMoveMode getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "MotorDuration [type=" + this.type + ", value=" + this.value + "]";
    }
}
