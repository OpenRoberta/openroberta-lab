package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_colour_getMode</b>, <b>robSensors_colour_getSample</b> and <b>robSensors_colour_setMode</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link ColorSensorMode}. See enum {@link ColorSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorSensorMode, SensorPort)}.<br>
 */
public class ColorSensor extends Sensor {
    private final ColorSensorMode mode;
    private final SensorPort port;

    private ColorSensor(ColorSensorMode mode, SensorPort port) {
        super(Phrase.Kind.COLOR_SENSING);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link ColorSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link ColorSensorMode} for all possible modes that the sensor have.
     * @param port on where the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     * @return read only object of class {@link ColorSensor}
     */
    public static ColorSensor make(ColorSensorMode mode, SensorPort port) {
        return new ColorSensor(mode, port);
    }

    /**
     * @return get the mode of sensor. See enum {@link ColorSensorMode} for all possible modes that the sensor have.
     */
    public ColorSensorMode getMode() {
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
                sb.append("hal.getColorSensorModeName(" + this.port.toString() + ")");
                break;
            case GET_SAMPLE:
                sb.append("hal.getColorSensorValue(" + this.port.toString() + ")");
                break;
            default:
                sb.append("hal.setColorSensorMode(" + this.port.toString() + ", " + this.mode.toString() + ");");
                break;
        }
    }

    @Override
    public String toString() {
        return "ColorSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }
}
