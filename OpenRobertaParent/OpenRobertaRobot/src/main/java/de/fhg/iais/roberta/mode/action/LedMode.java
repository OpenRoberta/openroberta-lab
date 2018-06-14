package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.ILedMode;

public enum LedMode implements ILedMode {
    ON(), OFF();

    private final String[] values;

    private LedMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}