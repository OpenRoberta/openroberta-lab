package de.fhg.iais.roberta.mode.sensor.vorwerk;

import de.fhg.iais.roberta.inter.mode.sensor.ISlot;

public enum Slot implements ISlot {
    NO_SLOT, EMPTY_SLOT( "" ), CENTER( "CENTER" ), LEFT( "LEFT" ), RIGHT( "RIGHT" ), FRONT( "FRONT" ), SIDE( "SIDE" );

    private final String[] values;

    private Slot(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
