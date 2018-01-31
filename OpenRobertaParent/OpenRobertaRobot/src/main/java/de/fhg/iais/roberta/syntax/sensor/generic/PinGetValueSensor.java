package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class represents the <b>mbedSensors_pin_getSample</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for reading values from a given pin.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class PinGetValueSensor<V> extends ExternalSensor<V> {

    private PinGetValueSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("PIN_VALUE"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinGetValueSensor}.
     *
     * @param port on which the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible ports that the sensor can be
     *        connected,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinGetValueSensor}
     */
    public static <V> PinGetValueSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PinGetValueSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitPinGetValueSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        SensorMetaDataBean sensorData = extractSensorPortAndMode(block, helper, helper.getModeFactory()::getPinGetValueSensorMode);
        return PinGetValueSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }
}
