package de.fhg.iais.roberta.ast.syntax.aktion;

public class MotionParam {
    private final int speed;
    private final int distance;

    private MotionParam(Builder mpb) {
        this.speed = mpb.speed;
        this.distance = mpb.distance;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getDistance() {
        return this.distance;
    }

    public static class Builder {
        private int speed;
        private int distance;

        public Builder speed(int speed) {
            this.speed = speed;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public MotionParam build() {
            return new MotionParam(this);
        }
    }
}
