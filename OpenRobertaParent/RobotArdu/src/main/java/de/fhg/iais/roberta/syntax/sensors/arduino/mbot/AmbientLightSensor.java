package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.MbotAstVisitor;

/**
 * This class represents the <b>robSensors_colour_getMode</b>, <b>robSensors_colour_getSample</b> and <b>robSensors_colour_setMode</b> blocks from Blockly into
 * the AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link LightSensorMode}. See enum {@link LightSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(LightSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class AmbientLightSensor<V> extends Sensor<V> {
    private final ISensorPort port;

    private AmbientLightSensor(ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("AMBIENT_LIGHT_GETSAMPLE"), properties, comment);
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link AmbientLightSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link LightSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link AmbientLightSensor}
     */
    static <V> AmbientLightSensor<V> make(ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AmbientLightSensor<V>(port, properties, comment);
    }

    public ISensorPort getPort() {
        return this.port;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbotAstVisitor<V>) visitor).visitAmbientLightSensor(this);
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
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        return AmbientLightSensor.make(factory.getSensorPort(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        jaxbDestination.setMutation(mutation);
        String fieldValue = getPort().getOraName();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "LightSensor [port=" + this.getPort() + "]";
    }

}
