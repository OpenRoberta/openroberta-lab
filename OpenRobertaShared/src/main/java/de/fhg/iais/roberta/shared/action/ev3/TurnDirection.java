package de.fhg.iais.roberta.shared.action.ev3;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Direction in which the robot will drive.
 */
public enum TurnDirection {
    RIGHT(), LEFT();

    private final String[] values;

    private TurnDirection(String... values) {
        this.values = values;
    }

    /**
     * Get direction from {@link TurnDirection} from string parameter. It is possible for one direction to have multiple string mappings.
     * Throws exception if the direction does not exists.
     *
     * @param name of the direction
     * @return name of the direction from the enum {@link TurnDirection}
     */
    public static TurnDirection get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid direction: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( TurnDirection sp : TurnDirection.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.values ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid direction: " + s);
    }
}