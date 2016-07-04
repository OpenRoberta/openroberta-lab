package de.fhg.iais.roberta.ev3.factory.action;

import de.fhg.iais.roberta.factory.IBrickLedColor;

public enum BrickLedColor implements IBrickLedColor {
    GREEN(), ORANGE(), RED();

    private final String[] values;

    private BrickLedColor(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}