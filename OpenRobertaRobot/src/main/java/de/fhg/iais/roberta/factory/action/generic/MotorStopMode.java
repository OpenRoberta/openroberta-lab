package de.fhg.iais.roberta.factory.action.generic;

import de.fhg.iais.roberta.factory.action.IMotorStopMode;

public enum MotorStopMode implements IMotorStopMode {
    NONFLOAT(), FLOAT();

    private final String[] values;

    private MotorStopMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}