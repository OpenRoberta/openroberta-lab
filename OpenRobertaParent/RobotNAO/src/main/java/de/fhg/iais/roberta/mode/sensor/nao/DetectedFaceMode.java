package de.fhg.iais.roberta.mode.sensor.nao;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum DetectedFaceMode implements IMode {
    NAMEONE( "NAMEONE" ), NAMEALL( "NAMEALL" );

    private final String[] values;

    private DetectedFaceMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}