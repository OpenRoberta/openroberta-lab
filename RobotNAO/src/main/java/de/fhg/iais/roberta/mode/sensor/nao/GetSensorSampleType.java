package de.fhg.iais.roberta.mode.sensor.nao;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>naoSensors_getSample</b> Blockly block.
 */
public enum GetSensorSampleType {
    NAO_TOUCHSENSOR, NAO_DETECTFACE, NAO_NAOMARK, NAO_SONAR, NAO_GYROMETER, NAO_ACCELEROMETER, NAO_FSR, NAO_RECOGNIZEWORD;

    private final String[] values;

    private GetSensorSampleType(String... values) {
        this.values = values;

    }

    /**
     * get mode from {@link MotorTachoMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link MotorTachoMode}
     */
    public static GetSensorSampleType get(String s) {
        if ( s == null || s.isEmpty() ) {
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
}