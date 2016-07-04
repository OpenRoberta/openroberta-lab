package de.fhg.iais.roberta.ev3.factory.action;

import de.fhg.iais.roberta.factory.IBlinkMode;

public enum BlinkMode implements IBlinkMode {
    ON(), FLASH(), DOUBLE_FLASH();

    private final String[] values;

    private BlinkMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}