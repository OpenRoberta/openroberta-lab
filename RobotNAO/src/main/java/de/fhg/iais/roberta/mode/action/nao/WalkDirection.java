package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum WalkDirection implements IMode {
    FOREWARD( "FORWARD" ), BACKWARD();

    private final String[] values;

    private WalkDirection(String... values) {
        this.values = values;
    }

    public static WalkDirection get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Walk Direction: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( WalkDirection wd : WalkDirection.values() ) {
            if ( wd.toString().equals(sUpper) ) {
                return wd;
            }
            for ( String value : wd.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return wd;
                }
            }
        }
        throw new DbcException("Invalid Walk Direction: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}
