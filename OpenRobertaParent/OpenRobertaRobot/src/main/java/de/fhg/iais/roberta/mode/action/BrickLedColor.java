package de.fhg.iais.roberta.mode.action;

import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;

public enum BrickLedColor implements IBrickLedColor {
    DEFAULT(), GREEN( "Green" ), ORANGE( "Orange" ), RED( "Red" ), BLUE( "Blue" );

    private final String[] values;

    private BrickLedColor(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}