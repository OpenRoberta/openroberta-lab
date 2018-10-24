package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum DetectMarkSensorMode implements IMode {
    IDONE( "IDONE" ), IDALL( "IDALL" );

    private final String[] values;

    private DetectMarkSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}