package de.fhg.iais.roberta.mode.sensor.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Sensor implements IMode {
    HAND(), HEAD(), BUMPER();

    private final String[] values;

    private Sensor(String... values) {
        this.values = values;
    }

    public static Sensor get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Side: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Sensor s : Sensor.values() ) {
            if ( s.toString().equals(sUpper) ) {
                return s;
            }
            for ( String value : s.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return s;
                }
            }
        }
        throw new DbcException("Invalid Side: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}