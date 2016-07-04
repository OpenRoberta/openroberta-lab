package de.fhg.iais.roberta.generic.factory;

import java.util.Locale;

import de.fhg.iais.roberta.factory.IIndexLocation;
import de.fhg.iais.roberta.util.dbc.DbcException;

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

    public static IndexLocation get(String indexLocation) {
        if ( indexLocation == null || indexLocation.isEmpty() ) {
            throw new DbcException("Invalid Index Location: " + indexLocation);
        }
        String sUpper = indexLocation.trim().toUpperCase(Locale.GERMAN);
        for ( IndexLocation po : IndexLocation.values() ) {
            if ( po.toString().equals(sUpper) ) {
                return po;
            }
            for ( String value : po.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return po;
                }
            }
        }
        throw new DbcException("Invalid Index Location: " + indexLocation);
    }

}