package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class represents the <b>robSensors_gyro_getMode</b>, <b>robSensors_gyro_getSample</b> and <b>robSensors_gyro_setMode</b> blocks from Blockly into the
 * AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link GyroSensorMode}. See enum {@link GyroSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(GyroSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class GyroSensor<V> extends ExternalSensor<V> {

    private GyroSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("GYRO_SENSING"), properties, comment);
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
    public static <V> GyroSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GyroSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitGyroSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        SensorMetaDataBean sensorMetaDataBean;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_GYRO_RESET) ) {
            List<Field> fields = helper.extractFields(block, (short) 1);
            String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorMetaDataBean =
                new SensorMetaDataBean(factory.getSensorPort(portName), factory.getGyroSensorMode("RESET"), factory.getSlot(BlocklyConstants.NO_SLOT), false);
            return GyroSensor.make(sensorMetaDataBean, helper.extractBlockProperties(block), helper.extractComment(block));
        }
        sensorMetaDataBean = extractSensorPortAndMode(block, helper, helper.getModeFactory()::getGyroSensorMode);
        return GyroSensor.make(sensorMetaDataBean, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        //TODO: move reset to another block and delete astToBlock() method from here
        String fieldValue = getPort().getOraName();
        if ( getMode().toString().equals("ANGLE") || getMode().toString().equals("RATE") ) {
            Mutation mutation = new Mutation();
            mutation.setMode(getMode().toString());
            jaxbDestination.setMutation(mutation);
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE, getMode().toString());
        } else if (getMode().toString().equals("TILTED")) {
            String fieldSlot = getSlot().toString();
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SLOT, fieldSlot);
        }
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        return jaxbDestination;
    }

}
