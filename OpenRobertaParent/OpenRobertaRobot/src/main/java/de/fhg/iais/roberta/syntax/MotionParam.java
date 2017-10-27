package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;

/**
 * This class is parameter class used to set the speed of a motor and the type of movement the motor {@link MotorDuration.Mode}.
 */
public class MotionParam<V> {
    private final Expr<V> speed;
    private final MotorDuration<V> duration;

    private MotionParam(Builder<V> mpb) {
        this.speed = mpb.speed;
        this.duration = mpb.duration;
    }

    /**
     * @return speed of the motor
     */
    public Expr<V> getSpeed() {
        return this.speed;
    }

    /**
     * @return duration of the motors movement
     */
    public MotorDuration<V> getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return "MotionParam [speed=" + this.speed + ", duration=" + this.duration + "]";
    }

    /**
     * Static class for building object of class {@link MotionParam}.
     */
    public static class Builder<V> {
        private Expr<V> speed;
        private MotorDuration<V> duration;

        /**
         * Set the speed of the motor.
         *
         * @param speed
         * @return reference returned so calls can be chained
         */
        public Builder<V> speed(Expr<V> speed) {
            this.speed = speed;
            return this;
        }

        /**
         * Sets the motor work duration.
         *
         * @param duration
         * @return reference returned so calls can be chained
         */
        public Builder<V> duration(MotorDuration<V> duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Returns a reference to the object being constructed or result being calculated by the builder.
         *
         * @return the object constructed by the builder.
         */
        public MotionParam<V> build() {
            return new MotionParam<V>(this);
        }

    }

}
