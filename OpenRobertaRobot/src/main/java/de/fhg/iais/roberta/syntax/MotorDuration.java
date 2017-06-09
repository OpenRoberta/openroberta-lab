package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;

/**
 * This class is parameter class used to set the type of the motor duration mode {@link MotorMoveMode} and value for the duration.
 */
public class MotorDuration<V> {
    private IMotorMoveMode type;
    private Expr<V> value;

    /**
     * This constructor creates correct object of the class {@link MotorDuration}.<br>
     * <br>
     * Client must provide type of the motor movement (<i>see enum {@link MotorMoveMode} for all possible motor movements</i>) and value for the movement e.g.
     * distance(cm), number of rotations...
     *
     * @param type of the motor movement
     * @param value for the movement
     */
    public MotorDuration(IMotorMoveMode type, Expr<V> value) {
        super();
        this.setType(type);
        this.value = value;
    }

    /**
     * Returns the value of the duration set by the client.
     *
     * @return value of the duration as {@link Expr}
     */
    public Expr<V> getValue() {
        return this.value;
    }

    /**
     * Set the value of the motor duration.
     *
     * @param value
     */
    public void setValue(Expr<V> value) {
        this.value = value;
    }

    /**
     * Get the mode of the motor duration. See enum {@link MotorMoveMode} for all possible modes.
     *
     * @return mode of the motor duration.
     */
    public IMotorMoveMode getType() {
        return this.type;
    }

    /**
     * Set the mode of the motor duration. See enum {@link MotorMoveMode} for all possible modes.
     */
    public void setType(IMotorMoveMode type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MotorDuration [type=" + this.type + ", value=" + this.value + "]";
    }
}
