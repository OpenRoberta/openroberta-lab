package de.fhg.iais.roberta.shared.action.ev3;

import java.util.Arrays;
import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Direction in which the robot will drive.
 */
public enum DriveDirection {
    FOREWARD( "OFF" ), BACKWARD( "ON", "BACKWARDS" );

    private final String[] values;

    private DriveDirection(String... values) {
        this.values = Arrays.copyOf(values, values.length);
        Arrays.sort(this.values);
    }

    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }

    private boolean attributesMatchAttributes(String... attributes) {
        for ( String attribute : attributes ) {
            attribute = attribute.toUpperCase();
            if ( Arrays.binarySearch(this.values, attribute) >= 0 ) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param attributes which direction should contain
     * @return {@link DriveDirection} which contains given attributes
     */
    public static DriveDirection attributesMatch(String... attributes) {
        for ( DriveDirection driveDirection : DriveDirection.values() ) {
            if ( driveDirection.attributesMatchAttributes(attributes) ) {
                return driveDirection;
            }
        }
        throw new DbcException("No hardware component matches attributes " + Arrays.toString(attributes));
    }

    /**
     * Get direction from {@link DriveDirection} from string parameter. It is possible for one direction to have multiple string mappings.
     * Throws exception if the direction does not exists.
     * 
     * @param name of the direction
     * @return name of the direction from the enum {@link DriveDirection}
     */
    public static DriveDirection get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid direction: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( DriveDirection sp : DriveDirection.values() ) {
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