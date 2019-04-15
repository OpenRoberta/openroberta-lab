package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Posture implements IMode {
    STAND(), STANDINIT(), STANDZERO(), SIT( "sit" ), SITRELAX(), CROUCH(), LYINGBACK(), LYINGBELLY(), REST();

    private final String[] values;

    private Posture(String... values) {
        this.values = values;
    }

    public static Posture get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Posture: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Posture p : Posture.values() ) {
            if ( p.toString().equals(sUpper) ) {
                return p;
            }
            for ( String value : p.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return p;
                }
            }
        }
        throw new DbcException("Invalid Posture: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}