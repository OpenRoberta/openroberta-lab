package de.fhg.iais.roberta.syntax.sensor.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.action.mbed.MbedPins;
import de.fhg.iais.roberta.mode.sensor.mbed.GetSampleType;
import de.fhg.iais.roberta.mode.sensor.mbed.ValueType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor.Mode;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

/**
 * This class represents the <b>robSensors_getSample</b> block from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorType} and port. See enum {@link SensorType} for all possible type of sensors.<br>
 * <br>
 * To create an instance from this class use the method {@link #make}.<br>
 */
public class MbedGetSampleSensor<V> extends Sensor<V> {
    private final Sensor<V> sensor;
    private final String sensorPort;
    private final GetSampleType sensorType;

    private MbedGetSampleSensor(GetSampleType sensorType, String port, BlocklyBlockProperties properties, BlocklyComment comment, IRobotFactory factory) {
        super(BlockTypeContainer.getByName("MBED_SENSOR_GET_SAMPLE"), properties, comment);
        Assert.isTrue(sensorType != null);
        this.sensorPort = port;
        this.sensorType = sensorType;
        switch ( sensorType.getSensorType() ) {
            case BlocklyConstants.KEY_PRESSED:
                this.sensor = BrickSensor.make(Mode.IS_PRESSED, factory.getBrickKey(port), properties, comment);
                break;
            case BlocklyConstants.PIN_TOUCHED:
                this.sensor = PinTouchSensor.make(MbedPins.findPin(port), properties, comment);
                break;
            case BlocklyConstants.PIN_ANALOG:
            case BlocklyConstants.PIN_DIGITAL:
            case BlocklyConstants.PIN_PULSEHIGH:
            case BlocklyConstants.PIN_PULSELOW:
                this.sensor = PinGetValueSensor.make(MbedPins.findPin(port), ValueType.get(sensorType.getSensorMode()), properties, comment);
                break;
            case BlocklyConstants.GESTURE_ACTIVE:
                this.sensor = GestureSensor.make(GestureSensor.GestureMode.valueOf(port), properties, comment);
                break;
            case BlocklyConstants.COMPASS:
                this.sensor =
                    CompassSensor
                        .make(factory.getCompassSensorMode(BlocklyConstants.DEFAULT), factory.getSensorPort(BlocklyConstants.NO_PORT), properties, comment);
                break;
            case BlocklyConstants.SOUND:
                this.sensor =
                    SoundSensor
                        .make(factory.getSoundSensorMode(BlocklyConstants.DEFAULT), factory.getSensorPort(BlocklyConstants.NO_PORT), properties, comment);
                break;
            case BlocklyConstants.TEMPERATURE:
                this.sensor =
                    TemperatureSensor
                        .make(factory.getTemperatureSensorMode(BlocklyConstants.DEFAULT), factory.getSensorPort(BlocklyConstants.NO_PORT), properties, comment);
                break;
            case BlocklyConstants.LIGHT_LEVEL:
                this.sensor =
                    LightSensor
                        .make(factory.getLightSensorMode(BlocklyConstants.DEFAULT), factory.getSensorPort(BlocklyConstants.NO_PORT), properties, comment);
                break;
            case BlocklyConstants.ACCELERATION:
                this.sensor = AccelerometerSensor.make(AccelerometerSensor.Mode.valueOf(port), properties, comment);
                break;
            case BlocklyConstants.ORIENTATION:
                this.sensor = AccelerometerOrientationSensor.make(AccelerometerOrientationSensor.Mode.valueOf(port), properties, comment);
                break;
            case BlocklyConstants.TIME:
                this.sensor = TimerSensor.make(factory.getTimerSensorMode("GET_SAMPLE"), Integer.valueOf(port), properties, comment);
                break;
            default:
                throw new DbcException("Invalid sensor " + sensorType.getSensorType() + "!");
        }
        setReadOnly();
    }

    /**
     * Create object of the class {@link MbedGetSampleSensor}.
     *
     * @param sensorType; must be <b>not</b> null,
     * @param port on which the sensor is connected; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MbedGetSampleSensor}
     */
    public static <V> MbedGetSampleSensor<V> make(
        GetSampleType sensorType,
        String port,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        IRobotFactory factory) {
        return new MbedGetSampleSensor<>(sensorType, port, properties, comment, factory);
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
        return "MbedGetSampleSensor [" + this.sensor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitMbedGetSampleSensor(this);
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
        final String portNameType = GetSampleType.get(modeName).getPortTypeName();
        String portName = "";
        if ( !portNameType.equals("") ) {
            portName = helper.extractField(fields, portNameType);
        }
        return MbedGetSampleSensor
            .make(GetSampleType.get(modeName), portName, helper.extractBlockProperties(block), helper.extractComment(block), helper.getModeFactory());
    }

    @Override
    public Block astToBlock() {
        final Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this.sensor, jaxbDestination);

        final Mutation mutation = new Mutation();
        mutation.setInput(getSensorType().name());
        jaxbDestination.setMutation(mutation);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, getSensorType().name());
        if ( !this.sensorPort.equals("") ) {
            JaxbTransformerHelper.addField(jaxbDestination, getSensorType().getPortTypeName(), this.sensorPort);
        }
        // TODO: This is hard coded on the blockly side
        final Data data = new Data();
        data.setValue("calliope");
        jaxbDestination.setData(data);
        return jaxbDestination;
    }

}
