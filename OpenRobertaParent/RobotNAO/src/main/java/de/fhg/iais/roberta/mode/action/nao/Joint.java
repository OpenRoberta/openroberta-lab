package de.fhg.iais.roberta.mode.action.nao;

import java.util.Locale;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum Joint implements IMode {
    HEADYAW(),
    HEADPITCH(),
    LSHOULDERPITCH(),
    LSHOULDERROLL(),
    LELBOWYAW(),
    LELBOWROLL(),
    LWRISTYAW(),
    LHAND(),
    LHIPYAWPITCH(),
    LHIPROLL(),
    LHIPPITCH(),
    LKNEEPITCH(),
    LANKLEPITCH(),
    RANKLEROLL(),
    RHIPYAWPITCH(),
    RHIPROLL(),
    RHIPPITCH(),
    RKNEEPITCH(),
    RANKLEPITCH(),
    LANKLEROLL(),
    RSHOULDERPITCH(),
    RSHOULDERROLL(),
    RELBOWYAW(),
    RELBOWROLL(),
    RWRISTYAW(),
    RHAND();

    private final String[] values;

    private Joint(String... values) {
        this.values = values;
    }

    public static Joint get(String direction) {
        if ( direction == null || direction.isEmpty() ) {
            throw new DbcException("Invalid Joint1: " + direction);
        }
        String sUpper = direction.trim().toUpperCase(Locale.GERMAN);
        for ( Joint bp : Joint.values() ) {
            if ( bp.toString().equals(sUpper) ) {
                return bp;
            }
            for ( String value : bp.getValues() ) {
                if ( sUpper.equals(value) ) {
                    return bp;
                }
            }
        }
        throw new DbcException("Invalid Joint2: " + direction);
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

}