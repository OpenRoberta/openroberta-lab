package de.fhg.iais.roberta.shared.action.ev3;

import java.util.Arrays;
import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum MotorSide {
    RIGHT( "right" ), LEFT( "left" ), NONE( "" );

    private final String[] values;

    private MotorSide(String... values) {
        Assert.isTrue(values.length > 0);
        this.values = Arrays.copyOf(values, values.length);
        for ( int i = 0; i < values.length; i++ ) {
            this.values[i] = this.values[i].toLowerCase();
        }
        Arrays.sort(this.values);
    }

    /**
     * @return textual representation
     */
    public String getText() {
        return this.values[0];
    }

    private boolean attributesMatchAttributes(String... attributes) {
        for ( String attribute : attributes ) {
            attribute = attribute.toLowerCase();
            if ( Arrays.binarySearch(this.values, attribute) >= 0 ) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param attributes which side should contain
     * @return {@link MotorSide} which contains given attributes
     */
    public static MotorSide attributesMatch(String... attributes) {
        for ( MotorSide motorSide : MotorSide.values() ) {
            if ( motorSide.attributesMatchAttributes(attributes) ) {
                return motorSide;
            }
        }
        throw new DbcException("No hardware component matches attributes " + Arrays.toString(attributes));
    }

    /**
     * Get the side of the motor from {@link MotorSide} from string parameter.
     * Throws exception if the motor side does not exists.
     *
     * @param name of the side
     * @return name of the side from the enum {@link MotorSide}
     */
    public static MotorSide get(String s) {
        if ( s == null || s.isEmpty() ) {
            throw new DbcException("Invalid motor side: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( MotorSide sp : MotorSide.values() ) {
            if ( sp.toString().equals(sUpper) ) {
                return sp;
            }
            for ( String value : sp.values ) {
                if ( sUpper.equals(value) ) {
                    return sp;
                }
            }
        }
        throw new DbcException("Invalid motor side: " + s);
    }
}
