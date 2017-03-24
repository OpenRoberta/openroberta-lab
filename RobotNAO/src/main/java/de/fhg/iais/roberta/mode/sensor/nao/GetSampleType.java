package de.fhg.iais.roberta.mode.sensor.nao;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This enumeration contain all types of sensors that are used in <b>robSensors_getSample</b> Blockly block.
 */
public enum GetSampleType {
    NAO_TOUCHSENSOR( "TOUCHSENSOR", "POSITION", "SIDE", "", "", "" ),
    NAO_DETECTFACE( "DETECTFACE", "", "", "", "", "" ),
    NAO_NAOMARK( "NAOMARK", "", "", "", "", "" ),
    NAO_SONAR( "SONAR", "", "", "", "", "" ),
    NAO_GYROMETER( "GYROMETER", "", "", "COORDINATE", "", "" ),
    NAO_ACCELEROMETER( "ACCELEROMETER", "", "", "COORDINATE", "", "" ),
    NAO_FSR( "FSR", "", "", "", "SIDE", "" ),
	NAO_RECOGNIZEDWORD("RECOGNIZEDWORD", "", "", "", "", "");

    private final String sensorType;
    private final String touchSensorName;
    private final String touchSideName;
    private final String coordinateName;
    private final String fsrSide;
    private final String[] values;

    public String getTouchSideName() {
        return this.touchSideName;
    }

    public String getCoordinateName() {
        return this.coordinateName;
    }

    public String getFsrSide() {
        return this.fsrSide;
    }

    private GetSampleType(
        String sensorType,
        String touchSensorName,
        String touchSideName,
        String coordinateName,
        String fsrSide,
        String... values) {
        this.values = values;
        this.sensorType = sensorType;
        this.touchSensorName = touchSensorName;
        this.touchSideName = touchSideName;
        this.coordinateName = coordinateName;
        this.fsrSide = fsrSide;
    }

    public String getTouchSensorName() {
        return this.touchSensorName;
    }

    public String getSensorType() {
        return this.sensorType;
    }

    /**
     * get mode from {@link MotorTachoMode} from string parameter. It is possible for one mode to have multiple string mappings.
     * Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link MotorTachoMode}
     */
    public static GetSampleType get(String s) {
        if ( s == null || s.isEmpty() ) {
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