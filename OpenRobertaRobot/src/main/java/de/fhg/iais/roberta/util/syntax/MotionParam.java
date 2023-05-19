package de.fhg.iais.roberta.util.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;

/**
 * This class is a parameter class used to set the speed of a motor and the mode of movement the motor {@link MotorDuration.Mode}.
 */
public class MotionParam {
    private final Expr speed;
    private final MotorDuration duration;

    private MotionParam(Builder mpb) {
        this.speed = mpb.speed;
        this.duration = mpb.duration;
    }

    /**
     * @return speed of the motor
     */
    public Expr getSpeed() {
        return this.speed;
    }

    /**
     * @return duration of the motors movement
     */
    public MotorDuration getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return "MotionParam [speed=" + this.speed + ", duration=" + this.duration + "]";
    }

    /**
     * Static class for building object of class {@link MotionParam}.
     */
    public static class Builder {
        private Expr speed;
        private MotorDuration duration;

        /**
         * Set the speed of the motor.
         *
         * @param speed
         * @return reference returned so calls can be chained
         */
        public Builder speed(Expr speed) {
            this.speed = speed;
            return this;
        }

        /**
         * Sets the motor work duration.
         *
         * @param duration
         * @return reference returned so calls can be chained
         */
        public Builder duration(MotorDuration duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Returns a reference to the object being constructed or result being calculated by the builder.
         *
         * @return the object constructed by the builder.
         */
        public MotionParam build() {
            return new MotionParam(this);
        }

    }

}
