package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.ILightMode;

public enum LightMode implements ILightMode {
    DEFAULT(), ON( "HIGH" ), OFF( "LOW" ), FLASH(), DOUBLE_FLASH();

    private final String[] values;

    private LightMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}