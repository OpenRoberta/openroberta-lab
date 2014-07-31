package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_gyro_getMode</b>, <b>robSensors_gyro_getSample</b> and <b>robSensors_gyro_setMode</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link GyroSensorMode}. See enum {@link GyroSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(GyroSensorMode, SensorPort)}.<br>
 */
public class GyroSensor extends Sensor {
    private final GyroSensorMode mode;
    private final SensorPort port;

    private GyroSensor(GyroSensorMode mode, SensorPort port) {
        super(Phrase.Kind.GYRO_SENSIG);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link GyroSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link GyroSensorMode} for all possible modes that the sensor have.
     * @param port on where the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     * @return read only object of {@link GyroSensor}
     */
    public static GyroSensor make(GyroSensorMode mode, SensorPort port) {
        return new GyroSensor(mode, port);
    }

    /**
     * @return get the mode of sensor. See enum {@link GyroSensorMode} for all possible modes that the sensor have.
     */
    public GyroSensorMode getMode() {
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
        switch ( this.mode ) {
            case GET_MODE:
                sb.append("hal.getGyroSensorModeName(" + this.port.toString() + ")");
                break;
            case GET_SAMPLE:
                sb.append("hal.getGyroSensorValue(" + this.port.toString() + ")");
                break;
            case RESET:
                sb.append("hal.resetGyroSensor(" + this.port.toString() + ");");
                break;
            default:
                sb.append("hal.setGyroSensorMode(" + this.port.toString() + ", " + this.mode.toString() + ");");
                break;
        }
    }

    @Override
    public String toString() {
        return "GyroSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }
}
