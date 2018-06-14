package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class represents the <b>robSensors_getSample</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorType} and port. See enum {@link SensorType} for all possible type of sensors.<br>
 * <br>
 * To create an instance from this class use the method {@link #make}.<br>
 */
public class GetSampleSensor<V> extends Sensor<V> {
    private final Sensor<V> sensor;
    private final String sensorPort;
    private final String slot;
    private final GetSampleType sensorType;
    private final boolean isPortInMutation;
    private static final ch.qos.logback.classic.Logger LOG = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AbstractCompilerWorkflow.class);

    @SuppressWarnings("unchecked")
    private GetSampleSensor(
        GetSampleType sensorType,
        String port,
        String slot,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        super(BlockTypeContainer.getByName("SENSOR_GET_SAMPLE"), properties, comment);
        LOG.setLevel(ch.qos.logback.classic.Level.TRACE);
        Assert.notNull(sensorType);
        Assert.notNull(port);
        this.sensorPort = port;
        this.slot = slot;
        this.sensorType = sensorType;
        this.isPortInMutation = isPortInMutation;
        this.sensor = (Sensor<V>) factory.createSensor(sensorType, port, slot, isPortInMutation, properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link GetSampleSensor}.
     *
     * @param sensorType; must be <b>not</b> null,
     * @param port on which the sensor is connected; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link GetSampleSensor}
     */
    public static <V> GetSampleSensor<V> make(
        GetSampleType sensorType,
        String port,
        String slot,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        return new GetSampleSensor<>(sensorType, port, slot, isPortInMutation, properties, comment, factory);
    }

    /**
     * @return the sensor
     */
    public Sensor<V> getSensor() {
        return this.sensor;
    }

    /**
     * @return name of the port
     */
    public String getSensorPort() {
        return this.sensorPort;
    }

    public String getSlot() {
        return this.slot;
    }

    /**
     * @return type of the sensor who will get the sample
     */
    public GetSampleType getSensorType() {
        return this.sensorType;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [" + this.sensor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitGetSampleSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 3);
        String modeName = helper.extractField(fields, BlocklyConstants.SENSORTYPE);
        String portName = helper.extractField(fields, GetSampleType.get(modeName).getPortTypeName());
        String slot = helper.extractField(fields, GetSampleType.get(modeName).getValues()[0], BlocklyConstants.NO_SLOT);
        boolean isPortInMutation = block.getMutation().getPort() != null;
        return GetSampleSensor.make(
            GetSampleType.get(modeName),
            portName,
            slot,
            isPortInMutation,
            helper.extractBlockProperties(block),
            helper.extractComment(block),
            helper.getModeFactory());
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this.sensor, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setInput(getSensorType().name());
        if ( this.isPortInMutation ) {
            mutation.setPort(this.sensorPort);
        }
        jaxbDestination.setMutation(mutation);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, this.sensorType.name());
        JaxbTransformerHelper.addField(jaxbDestination, getSensorType().getPortTypeName(), this.sensorPort);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SLOT, this.slot);

        return jaxbDestination;
    }

}
