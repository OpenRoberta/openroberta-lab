package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Led implements IMode {
    EYES(), LEFTEYE(), RIGHTEYE(), EARS(), LEFTEAR(), RIGHTEAR(), CHEST(), HEAD(), LEFTFOOT(), RIGHTFOOT(), ALL();

    private final String[] values;

    private Led(String... values) {
        this.values = values;
    }

    public static Led get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Led: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Led c : Led.values() ) {
            if ( c.toString().equals(sUpper) ) {
                return c;
            }
            for ( String value : c.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return c;
                }
            }
        }
        throw new DbcException("Invalid Led: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}