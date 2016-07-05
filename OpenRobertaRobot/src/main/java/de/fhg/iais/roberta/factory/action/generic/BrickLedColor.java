package de.fhg.iais.roberta.factory.action.generic;

import de.fhg.iais.roberta.factory.action.IBrickLedColor;

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