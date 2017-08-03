package de.fhg.iais.roberta.mode.action.mbed;

import de.fhg.iais.roberta.inter.mode.action.IMotorStopMode;

public enum MotorStopMode implements IMotorStopMode {
    NONFLOAT(), FLOAT(), SLEEP();

    private final String[] values;

    private MotorStopMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}