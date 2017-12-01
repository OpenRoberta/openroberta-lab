package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.ICoordinatesMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

public final class AccelerometerSensor<V> extends ExternalSensor<V> {

    private AccelerometerSensor(ICoordinatesMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(mode, port, BlockTypeContainer.getByName("ACCELEROMETER_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link Gyroscope}. This instance is read only and can not be modified.
     *
     * @param port {@link ActorPort} on which the motor is connected,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Gyroscope}
     */
    public static <V> AccelerometerSensor<V> make(ICoordinatesMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AccelerometerSensor<>(mode, port, properties, comment);
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstSensorsVisitor<V>) visitor).visitAccelerometer(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        final IRobotFactory factory = helper.getModeFactory();
        final SensorMetaDataBean sensorData = extractPortAndMode(block, helper);
        final String mode = sensorData.getMode();
        final String port = sensorData.getPort();
        return AccelerometerSensor
            .make(factory.getAccelerometerSensorMode(mode), factory.getSensorPort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public String toString() {
        return "Accelerometer [port = " + getPort() + ", coordinate  = " + getMode() + "]";
    }

}
