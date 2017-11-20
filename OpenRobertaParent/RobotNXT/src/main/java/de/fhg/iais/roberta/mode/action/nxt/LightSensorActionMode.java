package de.fhg.iais.roberta.mode.action.nxt;

import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;

public enum LightSensorActionMode implements ILightSensorActionMode {
    RED(), GREEN(), BLUE();

    private final String[] values;

    private LightSensorActionMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}