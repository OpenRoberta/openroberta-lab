package de.fhg.iais.roberta.mode.sensor.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Position implements IMode {
    FRONT(), MIDDLE(), REAR();

    private final String[] values;

    private Position(String... values) {
        this.values = values;
    }

    public static Position get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Position: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Position p : Position.values() ) {
            if ( p.toString().equals(sUpper) ) {
                return p;
            }
            for ( String value : p.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return p;
                }
            }
        }
        throw new DbcException("Invalid Position: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}