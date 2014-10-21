package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor.Mode;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

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
public class GetSampleSensor<V> extends Sensor<V> {
    private final Sensor<V> sensor;
    private final String sensorPort;
    private final SensorType sensorType;

    private GetSampleSensor(SensorType sensorType, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.SENSOR_GET_SAMPLE, properties, comment);
        Assert.isTrue(sensorType != null && port != "");
        this.sensorPort = port;
        this.sensorType = sensorType;
        switch ( SensorType.get(sensorType.name()) ) {
            case TOUCH:
                this.sensor = TouchSensor.make(SensorPort.get(port), properties, comment);
                break;
            case ULTRASONIC:
                this.sensor = UltrasonicSensor.make(UltrasonicSensorMode.GET_SAMPLE, SensorPort.get(port), properties, comment);
                break;
            case COLOUR:
                this.sensor = ColorSensor.make(ColorSensorMode.GET_SAMPLE, SensorPort.get(port), properties, comment);
                break;
            case INFRARED:
                this.sensor = InfraredSensor.make(InfraredSensorMode.GET_SAMPLE, SensorPort.get(port), properties, comment);
                break;
            case ENCODER:
                this.sensor = EncoderSensor.make(MotorTachoMode.GET_SAMPLE, ActorPort.get(port), properties, comment);
                break;
            case KEYS_PRESSED:
                this.sensor = BrickSensor.make(Mode.IS_PRESSED, BrickKey.get(port), properties, comment);
                break;
            case GYRO:
                this.sensor = GyroSensor.make(GyroSensorMode.GET_SAMPLE, SensorPort.get(port), properties, comment);
                break;
            case TIME:
                this.sensor = TimerSensor.make(TimerSensorMode.GET_SAMPLE, Integer.valueOf(port), properties, comment);
                break;
            default:
                throw new DbcException("Invalid sensor!");
        }
        setReadOnly();
    }

    /**
     * Create object of the class {@link GetSampleSensor}.
     *
     * @param sensorType
     * @param port on which the sensor is connected
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link GetSampleSensor}
     */
    public static <V> GetSampleSensor<V> make(SensorType sensorType, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GetSampleSensor<V>(sensorType, port, properties, comment);
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
    public SensorType getSensorType() {
        return this.sensorType;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [sensor=" + this.sensor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitGetSampleSensor(this);
    }

}
