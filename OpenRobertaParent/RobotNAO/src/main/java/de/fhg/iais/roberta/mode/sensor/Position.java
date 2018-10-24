package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum Position implements IMode {
    FRONT(), MIDDLE(), REAR();

    private final String[] values;

    private Position(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}