package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_infrared_getMode</b>, <b>robSensors_infrared_getSample</b> and <b>robSensors_infrared_setMode</b> blocks from Blockly
 * into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link InfraredSensorMode}. See enum {@link InfraredSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(InfraredSensorMode, SensorPort)}.<br>
 */
public class InfraredSensor extends Sensor {
    private final InfraredSensorMode mode;
    private final SensorPort port;

    private InfraredSensor(InfraredSensorMode mode, SensorPort port) {
        super(Phrase.Kind.INFRARED_SENSING);
        Assert.isTrue(mode != null);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }

    /**
     * Create object of the class {@link InfraredSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link InfraredSensorMode} for all possible modes that the sensor have.
     * @param port on where the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     * @return read only object of class {@link InfraredSensor}
     */
    public static InfraredSensor make(InfraredSensorMode mode, SensorPort port) {
        return new InfraredSensor(mode, port);
    }

    /**
     * @return get the mode of sensor. See enum {@link InfraredSensorMode} for all possible modes that the sensor have.
     */
    public InfraredSensorMode getMode() {
        return this.mode;
    }

    /**
     * @return get the port on which the sensor is connected. See enum {@link SensorPort} for all possible sensor ports.
     */
    public SensorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "InfraredSensor [mode=" + this.mode + ", port=" + this.port + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitInfraredSensor(this);
    }

}
