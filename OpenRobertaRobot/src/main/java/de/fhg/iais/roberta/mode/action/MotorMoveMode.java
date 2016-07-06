package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;

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