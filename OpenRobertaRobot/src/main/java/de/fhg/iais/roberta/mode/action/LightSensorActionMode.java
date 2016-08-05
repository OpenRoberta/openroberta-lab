package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;

public enum LightSensorActionMode implements ILightSensorActionMode {
    RED( "RED" ), GREEN( "GREEN" ), BLUE( "BLUE" );

    private final String[] values;

    private LightSensorActionMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return values;
    }

}