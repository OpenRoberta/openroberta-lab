package de.fhg.iais.roberta.factory.action.generic;

import de.fhg.iais.roberta.factory.action.IMotorMoveMode;

public enum MotorMoveMode implements IMotorMoveMode {
    ROTATIONS(), DEGREE(), DISTANCE();

    private final String[] values;

    private MotorMoveMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}