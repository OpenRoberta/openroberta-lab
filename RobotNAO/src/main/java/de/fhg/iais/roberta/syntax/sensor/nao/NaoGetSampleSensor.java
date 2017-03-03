package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.sensor.nao.GetSampleType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoSensors_getSample</b> block from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorType} and port. See enum {@link SensorType} for all possible type of sensors.<br>
 * <br>
 * To create an instance from this class use the method {@link #make}.<br>
 */
public class NaoGetSampleSensor<V> extends Sensor<V> {
    private final Sensor<V> sensor;
    private final GetSampleType sensorType;

    private NaoGetSampleSensor(GetSampleType sensorType, BlocklyBlockProperties properties, BlocklyComment comment, IRobotFactory factory) {
        super(BlockTypeContainer.getByName("GET_SAMPLE"), properties, comment);
        Assert.isTrue(sensorType != null);
        this.sensorType = sensorType;
        switch ( sensorType.getSensorType() ) {
            case BlocklyConstants.NAO_TOUCHSENSOR:
                this.sensor = Touchsensors.make(null, null, properties, comment);
                break;
            case BlocklyConstants.NAO_DETECTFACE:
                this.sensor = DetectFace.make(properties, comment);
                break;
            case BlocklyConstants.NAO_NAOMARK:
                this.sensor = NaoMark.make(properties, comment);
                break;
            case BlocklyConstants.NAO_SONAR:
                this.sensor = Sonar.make(properties, comment);
                break;
            case BlocklyConstants.NAO_GYROMETER:
                this.sensor = Gyrometer.make(null, properties, comment);
                break;
            case BlocklyConstants.NAO_ACCELEROMETER:
                this.sensor = Accelerometer.make(null, properties, comment);
                break;
            case BlocklyConstants.NAO_FSR:
                this.sensor = ForceSensor.make(null, properties, comment);
                break;

            default:
                throw new DbcException("Invalid sensor " + sensorType.getSensorType() + "!");
        }
        setReadOnly();
    }

    /**
     * Create object of the class {@link NaoGetSampleSensor}.
     *
     * @param sensorType; must be <b>not</b> null,
     * @param port on which the sensor is connected; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NaoGetSampleSensor}
     */
    public static <V> NaoGetSampleSensor<V> make(
        GetSampleType sensorType,
        String port,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        return new NaoGetSampleSensor<V>(sensorType, properties, comment, factory);
    }

    /**
     * @return the sensor
     */
    public Sensor<V> getSensor() {
        return this.sensor;
    }

    /**
     * @return type of the sensor who will get the sample
     */
    public GetSampleType getSensorType() {
        return this.sensorType;
    }

    @Override
    public String toString() {
        return "NaoGetSampleSensor [" + this.sensor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitNaoGetSampleSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String modeName = helper.extractField(fields, BlocklyConstants.SENSORTYPE);
        return NaoGetSampleSensor
            .make(GetSampleType.get(modeName), modeName, helper.extractBlockProperties(block), helper.extractComment(block), helper.getModeFactory());
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this.sensor, jaxbDestination);

        Mutation mutation = new Mutation();
        mutation.setInput(getSensorType().name());
        jaxbDestination.setMutation(mutation);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, getSensorType().name());

        // TODO: This is hard coded on the blockly side
        Data data = new Data();
        data.setValue("nao");
        jaxbDestination.setData(data);
        return jaxbDestination;
    }

}
