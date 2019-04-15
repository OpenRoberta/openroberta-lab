package de.fhg.iais.roberta.mode.general;

import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;

public enum IndexLocation implements IIndexLocation {

    FIRST(), LAST(), FROM_START( "FROMSTART" ), FROM_END( "FROMEND" ), RANDOM( "RANDOM" );
    private final String[] values;

    private IndexLocation(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}