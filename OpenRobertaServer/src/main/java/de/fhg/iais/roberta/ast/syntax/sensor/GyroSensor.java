package de.fhg.iais.roberta.ast.syntax.sensor;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_gyro_getMode</b>, <b>robSensors_gyro_getSample</b> and <b>robSensors_gyro_setMode</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link GyroSensorMode}. See enum {@link GyroSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(GyroSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class GyroSensor<V> extends Sensor<V> {
    private final GyroSensorMode mode;
    private final SensorPort port;

    private GyroSensor(GyroSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.GYRO_SENSING, properties, comment);
        Assert.isTrue(mode != null && port != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link GyroSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link GyroSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link GyroSensor}
     */
    static <V> GyroSensor<V> make(GyroSensorMode mode, SensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GyroSensor<V>(mode, port, properties, comment);
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
    public String toString() {
        return "GyroSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitGyroSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_GYRO_RESET) ) {
            List<Field> fields = helper.extractFields(block, (short) 1);
            String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
            return GyroSensor.make(GyroSensorMode.RESET, SensorPort.get(portName), helper.extractBlockProperties(block), helper.extractComment(block));
        }
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE_);
        return GyroSensor.make(GyroSensorMode.get(modeName), SensorPort.get(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = getPort().getPortNumber();
        if ( getMode() == GyroSensorMode.ANGLE || getMode() == GyroSensorMode.RATE ) {
            AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());
        }
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        return jaxbDestination;
    }
}
