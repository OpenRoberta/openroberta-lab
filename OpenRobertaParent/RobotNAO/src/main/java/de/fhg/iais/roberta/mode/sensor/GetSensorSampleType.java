package de.fhg.iais.roberta.mode.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>naoSensors_getSample</b> Blockly block.
 */
public enum GetSensorSampleType {
    NAO_TOUCHSENSOR, NAO_DETECTFACE, NAO_NAOMARK, NAO_SONAR, NAO_GYROMETER, NAO_ACCELEROMETER, NAO_FSR, NAO_RECOGNIZEWORD;

    private final String[] values;

    public static GetSensorSampleType get(String s) {
        if ( (s == null) || s.isEmpty() ) {
            throw new DbcException("Invalid mode: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( GetSensorSampleType mo : GetSensorSampleType.values() ) {
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

    private GetSensorSampleType(String... values) {
        this.values = values;

    }
}