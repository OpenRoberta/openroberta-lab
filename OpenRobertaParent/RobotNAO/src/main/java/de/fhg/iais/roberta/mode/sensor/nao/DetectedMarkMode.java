package de.fhg.iais.roberta.mode.sensor.nao;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum DetectedMarkMode implements IMode {
    IDONE( "IDONE" ), IDALL( "IDALL" );

    private final String[] values;

    private DetectedMarkMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}