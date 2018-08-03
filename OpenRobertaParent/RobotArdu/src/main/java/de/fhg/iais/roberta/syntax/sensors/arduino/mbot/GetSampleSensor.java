package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.sensor.arduino.mbot.GetSampleType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.MbotAstVisitor;

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
    private final GetSampleType sensorType;
    private final boolean isPortInMutation;

    private GetSampleSensor(
        GetSampleType sensorType,
        String port,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        super(BlockTypeContainer.getByName("MAKEBLOCK_SENSOR_GET_SAMPLE"), properties, comment);
        Assert.notNull(sensorType);
        Assert.nonEmptyString(port);
        this.sensorPort = port;
        this.sensorType = sensorType;
        this.isPortInMutation = isPortInMutation;
        SensorMetaDataBean sensorMetaDataBean;
        switch ( sensorType.getSensorType() ) {
            case BlocklyConstants.TOUCH:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getTouchSensorMode("TOUCH"),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        this.isPortInMutation);
                this.sensor = TouchSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.ULTRASONIC:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getUltrasonicSensorMode(sensorType.getMode()),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                this.sensor = UltrasonicSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.GYRO:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getGyroSensorMode(sensorType.getMode()),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        this.isPortInMutation);
                this.sensor = GyroSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.ACCELEROMETER:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getAxis(sensorType.getMode()),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        this.isPortInMutation);
                this.sensor = AccelerometerSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.TIME:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getAxis(sensorType.getMode()),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        this.isPortInMutation);
                this.sensor = TimerSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.LIGHT:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getLightSensorMode(sensorType.getMode()),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        this.isPortInMutation);
                this.sensor = LightSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.AMBIENTLIGHT:
                this.sensor = AmbientLightSensor.make(factory.getSensorPort(port), properties, comment);
                break;
            case BlocklyConstants.JOYSTICK:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getJoystickMode(sensorType.getMode()),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        this.isPortInMutation);
                this.sensor = Joystick.make(sensorType.getMode(), sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.FLAME:
                this.sensor = FlameSensor.make(factory.getSensorPort(port), properties, comment);
                break;
            case BlocklyConstants.PIRMOTION:
                this.sensor = PIRMotionSensor.make(factory.getSensorPort(port), properties, comment);
                break;
            case BlocklyConstants.TEMPERATURE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getTemperatureSensorMode(BlocklyConstants.DEFAULT),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        this.isPortInMutation);
                this.sensor = LightSensor.make(sensorMetaDataBean, properties, comment);
                break;
            case BlocklyConstants.VOLTAGE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(
                        factory.getSensorPort(port),
                        factory.getVoltageSensorMode(BlocklyConstants.DEFAULT),
                        factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                        isPortInMutation);
                this.sensor = LightSensor.make(sensorMetaDataBean, properties, comment);
                break;
            default:
                throw new DbcException("Invalid sensor " + sensorType.getSensorType() + "!");
        }
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
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        return new GetSampleSensor<>(sensorType, port, isPortInMutation, properties, comment, factory);
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

    /**
     * @return type of the sensor who will get the sample
     */
    public GetSampleType getSensorType() {
        return this.sensorType;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [sensor=" + this.sensor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbotAstVisitor<V>) visitor).visitMbotGetSampleSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        final List<Field> fields = helper.extractFields(block, (short) 2);
        final String modeName = helper.extractField(fields, BlocklyConstants.SENSORTYPE);
        final String portName = helper.extractField(fields, GetSampleType.get(modeName).getPort());
        boolean isPortInMutation = block.getMutation().getPort() != null;
        return GetSampleSensor.make(
            GetSampleType.get(modeName),
            portName,
            isPortInMutation,
            helper.extractBlockProperties(block),
            helper.extractComment(block),
            helper.getModeFactory());
    }

    @Override
    public Block astToBlock() {
        final Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this.sensor, jaxbDestination);

        final Mutation mutation = new Mutation();
        mutation.setInput(getSensorType().name());
        jaxbDestination.setMutation(mutation);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, getSensorType().name());
        JaxbTransformerHelper.addField(jaxbDestination, getSensorType().getPort(), getSensorPort());

        return jaxbDestination;
    }

}
