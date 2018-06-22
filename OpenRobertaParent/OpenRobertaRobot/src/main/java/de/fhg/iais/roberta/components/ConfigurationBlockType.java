package de.fhg.iais.roberta.components;

import java.util.Arrays;
import java.util.Locale;

import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.util.dbc.DbcException;

public enum ConfigurationBlockType implements IConfigurationBlockType {
    BUZZER( "robConf_buzzer" ),
    DROP( "robConf_drop" ),
    ENCODER( "robConf_encoder" ),
    GYRO( "robConf_gyro" ),
    HUMIDITY( "robConf_humidity" ),
    INFRARED( "robConf_infrared" ),
    KEY( "robConf_key" ),
    LCD( "robConf_lcd" ),
    LCDI2C( "robConf_lcdi2c" ),
    LED( "robConf_led" ),
    LIGHT( "robConf_light" ),
    MOISTURE( "robConf_moisture" ),
    MOTION( "robConf_motion" ),
    MOTOR( "robConf_motor" ),
    POTENTIOMETER( "robConf_potentiometer" ),
    PULSE( "robConf_pulse" ),
    RELAY( "robConf_relay" ),
    RFID( "robConf_rfid" ),
    RGBLED( "robConf_rgbled" ),
    SERVOMOTOR( "robConf_servo" ),
    STEPMOTOR( "robConf_stepmotor" ),
    TEMPERATURE( "robConf_temperature" ),
    ULTRASONIC( "robConf_ultrasonic" );

    private final String[] values;

    private ConfigurationBlockType(String... values) {
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
    public static ConfigurationBlockType get(String s) {
        if ( (s == null) || s.isEmpty() ) {
            throw new DbcException("Invalid sensor type: " + s);
        }
        String sUpper = s.trim().toUpperCase(Locale.GERMAN);
        for ( ConfigurationBlockType sp : ConfigurationBlockType.values() ) {
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
