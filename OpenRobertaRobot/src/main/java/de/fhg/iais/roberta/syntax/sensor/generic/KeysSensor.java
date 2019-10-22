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
 * This class represents the <b>robSensors_key_isPressed</b> and <b>robSensors_key_isPressedAndReleased</b> blocks from Blockly into the AST (abstract syntax
 * tree). Object from this class will generate code for checking if a button on the brick is pressed.<br/>
 * <br>
 * The client must provide the {@link BrickKey} and {@link Mode}. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Mode, BrickKey, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class KeysSensor<V> extends ExternalSensor<V> {

    private KeysSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("KEYS_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link KeysSensor}. This instance is read only and can not be modified.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link Mode} for all possible modes that the sensor have
     * @param key on the brick; must be <b>not</b> null; see enum {@link BrickKey} for all possible keys,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link KeysSensor}
     */
    public static <V> KeysSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new KeysSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISensorVisitor<V>) visitor).visitKeysSensor(this);
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
        return KeysSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }
}
