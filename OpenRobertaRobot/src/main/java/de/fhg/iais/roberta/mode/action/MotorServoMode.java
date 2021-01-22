package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IMotorServoMode;

public enum MotorServoMode implements IMotorServoMode {
    DEFAULT(), MIN(), MID(), MAX();

    private final String[] values;

    private MotorServoMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}