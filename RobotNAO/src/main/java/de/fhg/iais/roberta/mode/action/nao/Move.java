package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Move implements IMode {
    TAICHI(), WAVE(), BLINK(), WIPEFOREHEAD();

    private final String[] values;

    private Move(String... values) {
        this.values = values;
    }

    public static Move get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Move: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Move c : Move.values() ) {
            if ( c.toString().equals(sUpper) ) {
                return c;
            }
            for ( String value : c.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return c;
                }
            }
        }
        throw new DbcException("Invalid Move: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}