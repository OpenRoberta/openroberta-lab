package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * This class represents <b>robSensors_irseeker_getSample</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link IRSeekerSensorMode}. See enum {@link IRSeekerSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(IRSeekerSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class IRSeekerSensor<V> extends ExternalSensor<V> {

    private IRSeekerSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("IRSEEKER_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link IRSeekerSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link IRSeekerSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link IRSeekerSensor}
     */
    public static <V> IRSeekerSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new IRSeekerSensor<V>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISensorVisitor<V>) visitor).visitIRSeekerSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        SensorMetaDataBean sensorData = extractPortAndModeAndSlot(block, helper);
        return IRSeekerSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }

}
