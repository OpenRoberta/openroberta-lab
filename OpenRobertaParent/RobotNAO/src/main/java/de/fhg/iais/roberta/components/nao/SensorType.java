package de.fhg.iais.roberta.components.nao;

import java.util.Arrays;
import java.util.Locale;

import de.fhg.iais.roberta.components.ISensorType;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum SensorType implements ISensorType {
    COLOR( "robBrick_colour" ),
    TOUCH( "robBrick_touch" ),
    ULTRASONIC( "robBrick_ultrasonic" ),
    INFRARED( "robBrick_infrared" ),
    GYRO( "robBrick_gyro", "makeblockSensors_gyroscope_getSample" ),
    SOUND( "robBrick_sound" ),
    LIGHT( "robBrick_light" ),
    LINE_FOLLOWER( "makeblockSensors_light" ),
    AMBIENT_LIGHT( "makeblockSensors_ambientlight" ),
    COMPASS( "robBrick_compass" ),
    TEMPERATURE( "robBrick_temperature" ),
    FLAMESENSOR( "makeblockSensors_flameSensor_getSample" ),
    ACCELEROMETER( "makeblockSensors_accelerometer_getSample" ),
    JOYSTICK( "arduSensors_joystick_getSample" ),
    PIR_MOTION( "makeblockSensors_motionSensor_getSample" ),
    DETECT_MARK( "naoSensors_naoMark" ),
    NAOFACE( "naoSensors_detectFace" ),
    NAOSPEECH( "naoSensors_recognizeWord" ),
    VOLTAGE( "" ),
    TIMER( "" ),
    NONE( "" );

    private final String[] values;

    private SensorType(String... values) {
        this.values = Arrays.copyOf(values, values.length);
        Arrays.sort(this.values);
    }

    public String blocklyName() {
        return this.values[0];
    }

    /**
     * Get direction from {@link DriveDirection} from string parameter. It is possible for one direction to have multiple string mappings. Throws exception if
     * the direction does not exists.
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
