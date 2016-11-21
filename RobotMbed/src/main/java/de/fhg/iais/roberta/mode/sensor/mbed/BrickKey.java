package de.fhg.iais.roberta.mode.sensor.mbed;

import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;

public enum BrickKey implements IBrickKey {
    BUTTON_A( "A" ), BUTTON_B( "B" );

    private final String[] values;

    private BrickKey(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}