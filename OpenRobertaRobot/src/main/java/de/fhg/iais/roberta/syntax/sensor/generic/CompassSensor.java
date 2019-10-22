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
 * This class represents the <b>robSensors_compass_getSample</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for checking the sensor's output.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class CompassSensor<V> extends ExternalSensor<V> {

    private CompassSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("COMPASS_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link CompassSensor}.
     *
     * @param port on which the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible ports that the sensor can be
     *        connected,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link CompassSensor}
     */
    public static <V> CompassSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new CompassSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISensorVisitor<V>) visitor).visitCompassSensor(this);
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
        SensorMetaDataBean sensorMetaDataBean;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_COMPASS_CALIBRATE) ) {
            List<Field> fields = helper.extractFields(block, (short) 1);
            String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorMetaDataBean =
                new SensorMetaDataBean(factory.sanitizePort(portName), factory.getMode("CALIBRATE"), factory.sanitizeSlot(BlocklyConstants.NO_SLOT), false);
            return CompassSensor.make(sensorMetaDataBean, helper.extractBlockProperties(block), helper.extractComment(block));
        }
        SensorMetaDataBean sensorData = extractPortAndModeAndSlot(block, helper);
        return CompassSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = super.astToBlock();
        if ( getMode().toString().equals("CALIBRATE") ) {
            jaxbDestination = new Block();
            Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, getPort());
        }
        return jaxbDestination;
    }
}
