package de.fhg.iais.roberta.mode.sensor.raspberrypi;

public enum Slot {
    NO_SLOT, EMPTY_SLOT( "" ), CENTER( "CENTER" ), LEFT( "LEFT" ), RIGHT( "RIGHT" ), FRONT( "FRONT" ), SIDE( "SIDE" );

    private final String[] values;

    private Slot(String... values) {
        this.values = values;
    }

    public String[] getValues() {
        return this.values;
    }

}
