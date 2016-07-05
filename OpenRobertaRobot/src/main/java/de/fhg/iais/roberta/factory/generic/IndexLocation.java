package de.fhg.iais.roberta.factory.generic;

import de.fhg.iais.roberta.factory.IIndexLocation;

public enum IndexLocation implements IIndexLocation {
    FIRST(), LAST(), FROM_START( "FROMSTART" ), FROM_END( "FROMEND" ), RANDOM();

    private final String[] values;

    private IndexLocation(String... values) {
        this.values = values;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }
}