package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * This class represents the <b>robSensors_encoder_getMode</b>, <b>robSensors_encoder_getSample</b> and <b>robSensors_encoder_setMode</b> blocks from Blockly
 * into the AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link ActorPort} and {@link EncoderSensorMode}. See enum {@link EncoderSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(EncoderSensorMode, ActorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class EncoderSensor<V> extends ExternalSensor<V> {
    private EncoderSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("ENCODER_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link EncoderSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link EncoderSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link EncoderSensor}
     */
    public static <V> EncoderSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EncoderSensor<V>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISensorVisitor<V>) visitor).visitEncoderSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        SensorMetaDataBean sensorData;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_ENCODER_RESET) ) {
            List<Field> fields = helper.extractFields(block, (short) 1);
            String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorData =
                new SensorMetaDataBean(factory.sanitizePort(portName), factory.getMode("RESET"), factory.sanitizeSlot(BlocklyConstants.NO_SLOT), false);
            return EncoderSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
        }
        sensorData = extractPortAndModeAndSlot(block, helper);
        return EncoderSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        if ( getMode().toString().equals("RESET") ) {
            Block jaxbDestination = new Block();
            Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
            String fieldValue = getPort().toString();
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
            return jaxbDestination;
        } else {
            return super.astToBlock();
        }

    }
}
