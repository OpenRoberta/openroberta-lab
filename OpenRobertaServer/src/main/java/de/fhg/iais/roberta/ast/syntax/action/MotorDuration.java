package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.expr.Expr;

/**
 * This class is parameter class used to set the type of the motor duration mode {@link MotorMoveMode} and value for the duration.
 */
public class MotorDuration {
    private MotorMoveMode type;
    private Expr value;

    public MotorDuration(MotorMoveMode type, Expr value) {
        super();
        this.setType(type);
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
     * Set the value of the motor duration.
     * 
     * @param value
     */
    public void setValue(Expr value) {
        this.value = value;
    }

    /**
     * Get the mode of the motor duration. See enum {@link MotorMoveMode} for all possible modes.
     * 
     * @return mode of the motor duration.
     */
    public MotorMoveMode getType() {
        return this.type;
    }

    /**
     * Set the mode of the motor duration. See enum {@link MotorMoveMode} for all possible modes.
     */
    public void setType(MotorMoveMode type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MotorDuration [type=" + this.type + ", value=" + this.value + "]";
    }
}
