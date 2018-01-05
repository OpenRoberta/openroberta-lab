package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.sensor.ISlot;

public enum Slot implements ISlot {
    NO_SLOT, EMPTY_SLOT( "" ), SENSOR_TOP( "1" ), CENTER( "2" ), SENSOR_BOTTOM( "3" ), SENSOR_ANY( "0" );
    private final String[] values;

    private Slot(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
