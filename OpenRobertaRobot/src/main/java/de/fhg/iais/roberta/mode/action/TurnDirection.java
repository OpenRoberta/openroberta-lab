package de.fhg.iais.roberta.mode.action;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum TurnDirection implements ITurnDirection {
    RIGHT(), LEFT();

    private final String[] values;

    private TurnDirection(String... values) {
        this.values = values;
    }

    public static TurnDirection get(String direction) {
        if ( (direction == null) || direction.isEmpty() ) {
            throw new DbcException("Invalid Turn Direction: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( TurnDirection td : TurnDirection.values() ) {
            if ( td.toString().equals(sUpper) ) {
                return td;
            }
            for ( String value : td.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return td;
                }
            }
        }
        throw new DbcException("Invalid Turn Direction: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}