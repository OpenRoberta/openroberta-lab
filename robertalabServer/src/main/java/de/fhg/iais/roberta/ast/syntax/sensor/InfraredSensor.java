package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>robSensors_infrared_getMode</b>, <b>robSensors_infrared_getSample</b> and <b>robSensors_infrared_setMode</b> blocks from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link Mode}. See enum {@link Mode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Mode, SensorPort)}.<br>
 */
public class InfraredSensor extends Sensor {
    private final Mode mode;
    private final SensorPort port;

    private InfraredSensor(Mode mode, SensorPort port) {
        super(Phrase.Kind.InfraredSensor);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link InfraredSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link Mode} for all possible modes that the sensor have.
     * @param port on where the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     * @return read only object of class {@link InfraredSensor}
     */
    public static InfraredSensor make(Mode mode, SensorPort port) {
        return new InfraredSensor(mode, port);
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
    public SensorPort getPort() {
        return this.port;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.port + ")");
    }

    @Override
    public String toString() {
        return "InfraredSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum Mode {
        DISTANCE(), SEEK(), GET_MODE(), GET_SAMPLE();
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
