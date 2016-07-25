package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;

public enum LightSensorActionMode implements IMotorMoveMode {
    RED(), GREEN(), BLUE();

    private final String[] values;

    private LightSensorActionMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return values;
    }

}