package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_colour_getMode</b>, <b>robSensors_colour_getSample</b> and <b>robSensors_colour_setMode</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link ColorSensorMode}. See enum {@link ColorSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class ColorSensor<V> extends Sensor<V> {
    private final ColorSensorMode mode;
    private final SensorPort port;

    private ColorSensor(ColorSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.COLOR_SENSING, properties, comment);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link ColorSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link ColorSensorMode} for all possible modes that the sensor have.
     * @param port on where the sensor is connected. See enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ColorSensor}
     */
    public static <V> ColorSensor<V> make(ColorSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ColorSensor<V>(mode, port, properties, comment);
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
    public String toString() {
        return "ColorSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitColorSensor(this);
    }
}
