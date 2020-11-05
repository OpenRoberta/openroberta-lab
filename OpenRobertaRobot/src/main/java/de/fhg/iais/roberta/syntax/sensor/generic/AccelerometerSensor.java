package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

public final class AccelerometerSensor<V> extends ExternalSensor<V> {

    private AccelerometerSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("ACCELEROMETER_SENSING"), properties, comment);
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
    public static <V> AccelerometerSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AccelerometerSensor<>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISensorVisitor<V>) visitor).visitAccelerometer(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object. Special version to fix issue #924 with Calliope/Microbit <hide> problem
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> SensorMetaDataBean extractPortAndModeAndSlotForAccelerometer(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = AbstractJaxb2Ast.extractFields(block, (short) 3);
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        String portName = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.NO_PORT);
        String modeName = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.MODE, BlocklyConstants.DEFAULT);

        String robotGroup = helper.getRobotFactory().getGroup();
        boolean calliopeOrMicrobit = "calliope".equals(robotGroup) || "microbit".equals(robotGroup);
        String slotName;
        if ( calliopeOrMicrobit ) {
            slotName = AbstractJaxb2Ast.extractNonEmptyField(fields, BlocklyConstants.SLOT, BlocklyConstants.X);
        } else {
            slotName = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.SLOT, BlocklyConstants.NO_SLOT);
        }

        boolean isPortInMutation = block.getMutation() != null && block.getMutation().getPort() != null;

        return new SensorMetaDataBean(factory.sanitizePort(portName), factory.getMode(modeName), factory.sanitizeSlot(slotName), isPortInMutation);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        SensorMetaDataBean sensorData = extractPortAndModeAndSlotForAccelerometer(block, helper);
        return AccelerometerSensor.make(sensorData, AbstractJaxb2Ast.extractBlockProperties(block), AbstractJaxb2Ast.extractComment(block));
    }
}
