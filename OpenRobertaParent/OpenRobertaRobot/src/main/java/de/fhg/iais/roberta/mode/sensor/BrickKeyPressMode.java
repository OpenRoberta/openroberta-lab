package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IBirckKeyPressMode;

public enum BrickKeyPressMode implements IBirckKeyPressMode {
    PRESSED, WAIT_FOR_PRESS, WAIT_FOR_PRESS_AND_RELEASE;
    private final String[] values;

    private BrickKeyPressMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
