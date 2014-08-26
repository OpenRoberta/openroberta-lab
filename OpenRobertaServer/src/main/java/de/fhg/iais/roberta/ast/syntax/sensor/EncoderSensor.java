package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_encoder_getMode</b>, <b>robSensors_encoder_getSample</b> and <b>robSensors_encoder_setMode</b> blocks from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link ActorPort} and {@link MotorTachoMode}. See enum {@link MotorTachoMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(MotorTachoMode, ActorPort)}.<br>
 */
public class EncoderSensor<V> extends Sensor<V> {
    private final MotorTachoMode mode;
    private final ActorPort motor;

    private EncoderSensor(MotorTachoMode mode, ActorPort motor) {
        super(Phrase.Kind.ENCODER_SENSING);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.motor = motor;
        setReadOnly();
    }

    /**
     * Create object of the class {@link EncoderSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link MotorTachoMode} for all possible modes that the sensor have.
     * @param port on where the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     * @return read only object of {@link EncoderSensor}
     */
    public static <V> EncoderSensor<V> make(MotorTachoMode mode, ActorPort motor) {
        return new EncoderSensor<V>(mode, motor);
    }

    /**
     * @return get the mode of sensor. See enum {@link MotorTachoMode} for all possible modes that the sensor have.
     */
    public MotorTachoMode getMode() {
        return this.mode;
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     */
    public ActorPort getMotor() {
        return this.motor;
    }

    @Override
    public String toString() {
        return "DrehSensor [mode=" + this.mode + ", motor=" + this.motor + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitEncoderSensor(this);
    }
}
