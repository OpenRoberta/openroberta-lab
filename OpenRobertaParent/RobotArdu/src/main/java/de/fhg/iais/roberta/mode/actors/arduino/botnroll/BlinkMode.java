package de.fhg.iais.roberta.mode.actors.arduino.botnroll;

import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;

public enum BlinkMode implements IBlinkMode {
    ON(), OFF(), DOUBLE_FLASH();

    private final String[] values;

    private BlinkMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}