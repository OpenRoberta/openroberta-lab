package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_ultrasonic_getMode</b>, <b>robSensors_ultrasonic_getSample</b> and <b>robSensors_ultrasonic_setMode</b> blocks from
 * Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link UltrasonicSensorMode}. See enum {@link UltrasonicSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(UltrasonicSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class UltrasonicSensor<V> extends Sensor<V> {
    private final UltrasonicSensorMode mode;
    private final SensorPort port;

    private UltrasonicSensor(UltrasonicSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.ULTRASONIC_SENSING, properties, comment);
        Assert.isTrue(mode != null && port != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link UltrasonicSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link UltrasonicSensorMode} for all possible modes that the sensor
     *        have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link UltrasonicSensor}
     */
    public static <V> UltrasonicSensor<V> make(UltrasonicSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new UltrasonicSensor<V>(mode, port, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link UltrasonicSensorMode} for all possible modes that the sensor have
     */
    public UltrasonicSensorMode getMode() {
        return this.mode;
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports
     */
    public SensorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "UltraSSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitUltrasonicSensor(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setMode(getMode().name());
        jaxbDestination.setMutation(mutation);
        String fieldValue = getPort().getPortNumber();
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }
}
