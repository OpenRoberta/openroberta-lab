package de.fhg.iais.roberta.components;

import java.util.Arrays;
import java.util.Locale;

import de.fhg.iais.roberta.shared.action.DriveDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum SensorType {
    COLOR( "robBrick_colour" ), TOUCH( "robBrick_touch" ), ULTRASONIC( "robBrick_ultrasonic" ), INFRARED( "robBrick_infrared" ), GYRO( "robBrick_gyro" );

    private final String[] values;

    private SensorType(String... values) {
        this.values = Arrays.copyOf(values, values.length);
        Arrays.sort(this.values);
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

    public String blocklyName() {
        return this.values[0];
    }

    /**
     * @param attributes which direction should contain
     * @return {@link DriveDirection} which contains given attributes
     */
    public static SensorType attributesMatch(String... attributes) {
        for ( SensorType driveDirection : SensorType.values() ) {
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
    public static SensorType get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid sensor type: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( SensorType sp : SensorType.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.values ) {
                if ( s.trim().equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid sensor type: " + s);
    }

}
