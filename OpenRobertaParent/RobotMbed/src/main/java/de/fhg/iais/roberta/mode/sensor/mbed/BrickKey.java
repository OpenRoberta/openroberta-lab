package de.fhg.iais.roberta.mode.sensor.mbed;

import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;

public enum BrickKey implements IBrickKey {
    button_a( "A" ), button_b( "B" );

    private final String[] values;

    private BrickKey(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}