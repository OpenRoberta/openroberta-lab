package de.fhg.iais.roberta.mode.sensor;

import de.fhg.iais.roberta.inter.mode.general.IMode;

public enum Resolution implements IMode {
    LOW( "0" ), MED( "1" ), HIGH( "2" );

    private final String[] values;

    private Resolution(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}