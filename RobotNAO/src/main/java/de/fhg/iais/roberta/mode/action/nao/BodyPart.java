package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum BodyPart implements IMode {
    BODY(), HEAD(), ARMS(), LEFTARM(), RIGHTARM(), LEGS(), LEFTLEG(), RIHTLEG();

    private final String[] values;

    private BodyPart(String... values) {
        this.values = values;
    }

    public static BodyPart get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Body part: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( BodyPart bp : BodyPart.values() ) {
            if ( bp.toString().equals(sUpper) ) {
                return bp;
            }
            for ( String value : bp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return bp;
                }
            }
        }
        throw new DbcException("Invalid Body part: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}