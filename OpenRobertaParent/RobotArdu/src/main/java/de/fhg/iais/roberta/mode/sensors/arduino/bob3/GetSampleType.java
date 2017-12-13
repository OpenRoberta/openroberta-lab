package de.fhg.iais.roberta.mode.sensors.arduino.bob3;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum GetSampleType {
    TOUCH( "ARM", "ARMPAIR", "TOUCH" ),
    TIME( "", "", "TIME" ),
    LIGHT_LEVEL( "", "", "LIGHT_LEVEL" ),
    TEMPERATURE( "", "", "TEMPERATURE" ),
    CODE( "", "", "CODE" );

    private final String armSide;
    private final String armPart;
    private final String sensorType;
    private final String[] values;

    private GetSampleType(String armSide, String armPart, String sensorType, String... values) {
        this.values = values;
        this.armSide = armSide;
        this.armPart = armPart;
        this.sensorType = sensorType;
    }

    /**
     * @return arm side
     */
    public String getArmSide() {
        return this.armSide;
    }

    public String getSensorType() {
        return this.sensorType;
    }

    /**
     * @return arm part
     */
    public String getArmPart() {
        return this.armPart;
    }

    /**
     * @param arm side
     * @param arm part
     * @return sensor type
     */
    public static GetSampleType get(String s) {
        if ( (s == null) || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( GetSampleType mo : GetSampleType.values() ) {
            if ( mo.toString().equals(sUpper) ) {
                return mo;
            }
            for ( String value : mo.values ) {
                if ( sUpper.equals(value) ) {
                    return mo;
                }
            }
        }
        throw new DbcException("Invalid mode: " + s);
    }
}
