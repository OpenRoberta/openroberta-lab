package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class GetSampleSensor<V> extends Sensor<V> {
    private final SensorType sensorType;
    private final String port;

    private GetSampleSensor(SensorType sensorType, String port, boolean disabled, String comment) {
        super(Phrase.Kind.GET_SAMPLE_SENSING, disabled, comment);
        Assert.isTrue(sensorType != null && port != "");
        this.sensorType = sensorType;
        this.port = port;
        setReadOnly();
    }

    public static <V> GetSampleSensor<V> make(SensorType sensorType, String port, boolean disabled, String comment) {
        return new GetSampleSensor<V>(sensorType, port, disabled, comment);
    }

    public SensorType getSensorType() {
        return this.sensorType;
    }

    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [sensorType=" + this.sensorType + ", port=" + this.port + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitGetSampleSensor(this);
    }

    public enum SensorType {
        TOUCH( "SENSORPORT", "touch sensor (gedrückt)" ), ULTRASONIC( "SENSORPORT", "ultrasonic sensor" ), COLOUR( "SENSORPORT", "colour sensor" ), INFRARED(
            "SENSORPORT",
            "infrared sensor" ), ENCODER( "MOTORPORT", "encoder" ), KEYS_PRESSED( "KEY", "brick button (gedrückt)" ), KEYS_PRESSED_RELEASED(
            "KEY",
            "brick button (geklickt)" ), GYRO( "SENSORPORT", "gyroscope" ), TIME( "SENSORNUM", "time" );
        private final String[] values;
        private final String portTypeName;

        private SensorType(String portTypeName, String... values) {
            this.values = values;
            this.portTypeName = portTypeName;
        }

        public String getPortTypeName() {
            return this.portTypeName;
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
