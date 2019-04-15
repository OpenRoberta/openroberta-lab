package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum DetectFaceSensorMode implements IMode {
    NAMEONE( "NAMEONE" ), NAMEALL( "NAMEALL" );

    private final String[] values;

    private DetectFaceSensorMode(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}