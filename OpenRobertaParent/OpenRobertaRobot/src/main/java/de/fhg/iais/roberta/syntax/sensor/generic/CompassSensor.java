package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
 * This class represents the <b>robSensors_touch_isPressed</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for checking if the sensor is pressed.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class CompassSensor<V> extends BaseSensor<V> {
    private final ISensorPort port;

    private CompassSensor(ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("COMPASS_SENSING"), properties, comment);
        this.port = port;
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
    public static <V> CompassSensor<V> make(ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new CompassSensor<>(port, properties, comment);
    }

    @Override
    public ISensorPort getPort() {
        return this.port;
    }

    /**
     * @return the mode
     */

    @Override
    public String toString() {
        return "CompassSensor []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitCompassSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String port = "1";
        if ( !block.getType().equals("robSensors_compass_getSample") && !block.getType().equals("mbedSensors_compass_getSample") ) {
            port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        }
        return CompassSensor.make(helper.getModeFactory().getSensorPort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        if ( !getProperty().getBlockType().equals("robSensors_compass_getSample") && !getProperty().getBlockType().equals("mbedSensors_compass_getSample") ) {
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, this.port.getPortNumber());
        }
        return jaxbDestination;
    }
}
