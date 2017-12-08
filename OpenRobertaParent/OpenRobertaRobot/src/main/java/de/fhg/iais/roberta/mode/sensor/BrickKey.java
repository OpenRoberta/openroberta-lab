package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;

public enum BrickKey implements IBrickKey {
    ENTER(), LEFT(), RIGHT(), ANY(), BUTTON_A( "A" ), BUTTON_B( "B" ), UP(), DOWN(), ESCAPE();

    private final String[] values;

    private BrickKey(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}