package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.expr.Expr;

/**
 * This class is parameter class used to set the speed of a motor and the type of movement the motor {@link MotorDuration.Mode}.
 */
public class MotionParam {
    private final Expr speed;
    private final MotorDuration duration;

    private MotionParam(Builder mpb) {
        this.speed = mpb.speed;
        this.duration = mpb.duration;
    }

    @Override
    public String toString() {
        return "MotionParam [speed=" + this.speed + ", duration=" + this.duration + "]";
    }

    public Expr getSpeed() {
        return this.speed;
    }

    public MotorDuration getDuration() {
        return this.duration;
    }

    public static class Builder {
        private Expr speed;
        private MotorDuration duration;

        public Builder speed(Expr speed) {
            this.speed = speed;
            return this;
        }

        public Builder duration(MotorDuration duration) {
            this.duration = duration;
            return this;
        }

        public MotionParam build() {
            return new MotionParam(this);
        }
    }

}
