package de.fhg.iais.roberta.mode.sensor.wedo;

import de.fhg.iais.roberta.inter.mode.sensor.ISlot;

public enum Slot implements ISlot {
    NO_SLOT, EMPTY_SLOT( "" ), UP( "UP" ), DOWN( "DOWN" ), BACK( "BACK" ), FRONT( "FRONT" ), NO( "NO" ), ANY( "ANY" );

    private final String[] values;

    private Slot(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}