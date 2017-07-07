package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class represents the <b>mbedSensors_temperature_getSample</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate
 * code for checking if the sensor is pressed.<br/>
 * <br>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class TemperatureSensor<V> extends BaseSensor<V> {

    private TemperatureSensor(ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("TEMP_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link TemperatureSensor}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link TemperatureSensor}
     */
    public static <V> TemperatureSensor<V> make(ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TemperatureSensor<V>(port, properties, comment);
    }

    @Override
    public String toString() {
        return "TemperatureSensor [" + this.getPort() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitTemperatureSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        if ( block.getType().equals(BlocklyConstants.MBED_SENSOR_TEMPERATURE_GET_SAMPLE) ) {
            return TemperatureSensor.make(null, helper.extractBlockProperties(block), helper.extractComment(block));
        } else {
            IRobotFactory factory = helper.getModeFactory();
            List<Field> fields = helper.extractFields(block, (short) 1);
            String port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
            return TemperatureSensor.make(factory.getSensorPort(port), helper.extractBlockProperties(block), helper.extractComment(block));
        }

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        ISensorPort port = this.getPort();
        if ( port != null ) {
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, port.getPortNumber());
        }
        return jaxbDestination;
    }
}
