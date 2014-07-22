package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>robSensors_encoder_getMode</b>, <b>robSensors_encoder_getSample</b> and <b>robSensors_encoder_setMode</b> blocks from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link ActorPort} and {@link Mode}. See enum {@link Mode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Mode, ActorPort)}.<br>
 */
public class DrehSensor extends Sensor {
    private final Mode mode;
    private final ActorPort motor;

    private DrehSensor(Mode mode, ActorPort motor) {
        super(Phrase.Kind.RotationSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.motor = motor;
        setReadOnly();
    }

    /**
     * Create object of the class {@link DrehSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link Mode} for all possible modes that the sensor have.
     * @param port on where the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     * @return read only object of {@link DrehSensor}
     */
    public static DrehSensor make(Mode mode, ActorPort motor) {
        return new DrehSensor(mode, motor);
    }

    /**
     * @return get the mode of sensor. See enum {@link Mode} for all possible modes that the sensor have.
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     */
    public ActorPort getMotor() {
        return this.motor;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.motor + ")");
    }

    @Override
    public String toString() {
        return "DrehSensor [mode=" + this.mode + ", motor=" + this.motor + "]";
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum Mode {
        ROTATION(), DEGREE(), GET_MODE(), GET_SAMPLE(), RESET();

        private final String[] values;

        private Mode(String... values) {
            this.values = values;
        }

        /**
         * get mode from {@link Mode} from string parameter. It is possible for one mode to have multiple string mappings.
         * Throws exception if the mode does not exists.
         * 
         * @param name of the mode
         * @return mode from the enum {@link Mode}
         */
        public static Mode get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid mode: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Mode mo : Mode.values() ) {
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
