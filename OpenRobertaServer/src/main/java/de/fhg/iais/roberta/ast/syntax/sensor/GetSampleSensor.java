package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class GetSampleSensor<V> extends Sensor<V> {
    private final SensorType sensorType;
    private final SensorPort sensorPort;

    private GetSampleSensor(SensorType sensorType, SensorPort sensorPort, boolean disabled, String comment) {
        super(Phrase.Kind.GET_SAMPLE_SENSING, disabled, comment);
        Assert.isTrue(sensorType != null && sensorPort != null);
        this.sensorType = sensorType;
        this.sensorPort = sensorPort;
        setReadOnly();
    }

    public static <V> GetSampleSensor<V> make(SensorType sensorType, SensorPort sensorPort, boolean disabled, String comment) {
        return new GetSampleSensor<V>(sensorType, sensorPort, disabled, comment);
    }

    public SensorType getSensorType() {
        return this.sensorType;
    }

    public SensorPort getSensorPort() {
        return this.sensorPort;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [sensorType=" + this.sensorType + ", sensorPort=" + this.sensorPort + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitGetSampleSensor(this);
    }

    public enum SensorType {
        TOUCH( "touch sensor (gedrückt)" ),
        ULTRASONIC( "ultrasonic sensor" ),
        COLOUR( "colour sensor" ),
        INFRARED( "infrared sensor" ),
        ENCODER( "encoder" ),
        KEYS_PRESSED( "brick button (gedrückt)" ),
        KEYS_PRESSED_RELEASED( "brick button (geklickt)" ),
        GYRO( "gyroscope" ),
        TIME( "time" );
        private final String[] values;

        private SensorType(String... values) {
            this.values = values;
        }

        public String getJavaCode() {
            return this.getClass().getSimpleName() + "." + this;
        }

        /**
         * get mode from {@link MotorTachoMode} from string parameter. It is possible for one mode to have multiple string mappings.
         * Throws exception if the mode does not exists.
         * 
         * @param name of the mode
         * @return mode from the enum {@link MotorTachoMode}
         */
        public static SensorType get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid mode: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( SensorType mo : SensorType.values() ) {
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
}
