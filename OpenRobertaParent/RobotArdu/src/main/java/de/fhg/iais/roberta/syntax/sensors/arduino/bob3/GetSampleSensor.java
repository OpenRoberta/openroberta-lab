package de.fhg.iais.roberta.syntax.sensors.arduino.bob3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.sensors.arduino.bob3.GetSampleType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.Bob3AstVisitor;

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
    private final String port;
    private final String slot;
    private final GetSampleType sensorType;
    private final boolean isPortInMutation;

    private GetSampleSensor(
        GetSampleType sensorType,
        String port,
        String slot,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        super(BlockTypeContainer.getByName("SENSOR_GET_SAMPLE"), properties, comment);
        Assert.isTrue(sensorType != null);
        this.port = port;
        this.slot = slot;
        this.sensorType = sensorType;
        this.isPortInMutation = isPortInMutation;
        SensorMetaDataBean sensorMetaDataBean;

        switch ( sensorType.getSensorType() ) {
            case BlocklyConstants.TOUCH:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getTimerSensorMode(BlocklyConstants.VALUE),
                        factory.getSlot(slot),
                        this.isPortInMutation);
                this.sensor = TouchSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.TIME:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(BlocklyConstants.NO_PORT),
                        factory.getTimerSensorMode(BlocklyConstants.VALUE),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                this.sensor = TimerSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.LIGHT_LEVEL:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(BlocklyConstants.NO_PORT),
                        factory.getLightSensorMode(BlocklyConstants.DEFAULT),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                this.sensor = LightSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.TEMPERATURE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getTemperatureSensorMode(BlocklyConstants.DEFAULT),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                this.sensor = TemperatureSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.CODE:
                this.sensor = CodePadSensor.make(properties, comment);
                break;
            default:
                throw new DbcException("Invalid sensor " + sensorType.getSensorType() + "!");
        }
        setReadOnly();
    }

    /**
     * Create object of the class {@link GetSampleSensor}.
     *
     * @param isPortInMutation
     * @param sensorType; must be <b>not</b> null,
     * @param port on which the sensor is connected; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link GetSampleSensor}
     */
    public static <V> GetSampleSensor<V> make(
        GetSampleType sensorType,
        String armSide,
        String armPart,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        return new GetSampleSensor<>(sensorType, armSide, armPart, isPortInMutation, properties, comment, factory);
    }

    /**
     * @return the sensor
     */
    public Sensor<V> getSensor() {
        return this.sensor;
    }

    /**
     * @return arm side
     */
    public String getArmSide() {
        return this.port;
    }

    public GetSampleType getSensorType() {
        return this.sensorType;
    }

    /**
     * @return arm part
     */
    public String getArmPart() {
        return this.slot;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [sensor=" + this.sensor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((Bob3AstVisitor<V>) visitor).visitBob3GetSampleSensor(this);
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
        String armSide = "";
        String armPart = "";
        String modeName = helper.extractField(fields, BlocklyConstants.SENSORTYPE);
        String portNameType = GetSampleType.get(modeName).getArmPart();
        if ( !portNameType.equals("") ) {
            armSide = helper.extractField(fields, BlocklyConstants.ARMSIDE);
            armPart = helper.extractField(fields, BlocklyConstants.ARMPART);
        }
        boolean isPortInMutation = block.getMutation().getPort() != null;
        return GetSampleSensor.make(
            GetSampleType.get(modeName),
            armSide,
            armPart,
            isPortInMutation,
            helper.extractBlockProperties(block),
            helper.extractComment(block),
            helper.getModeFactory());
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this.sensor, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, getSensorType().name());
        String portNameType = GetSampleType.get(getSensorType().name()).getArmPart();
        if ( !portNameType.equals("") ) {
            JaxbTransformerHelper.addField(jaxbDestination, getSensorType().getArmSide(), getSensorType().getArmPart());
        }

        return null;
    }

}
