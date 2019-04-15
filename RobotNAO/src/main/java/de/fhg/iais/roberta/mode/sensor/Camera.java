package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum Camera implements IMode {
    TOP( "0" ), BOTTOM( "1" );

    private final String[] values;

    private Camera(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}