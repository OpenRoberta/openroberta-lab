package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

/**
 * This class represents the <b>robSensors_gyro_getMode</b>, <b>robSensors_gyro_getSample</b> and <b>robSensors_gyro_setMode</b> blocks from Blockly into the
 * AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link GyroSensorMode}. See enum {@link GyroSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(GyroSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class GyroSensor<V> extends ExternalSensor<V> {

    private GyroSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("GYRO_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link GyroSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link GyroSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link GyroSensor}
     */
    public static <V> GyroSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GyroSensor<>(sensorMetaDataBean, properties, comment);
    }

    /**
     * Transformation from JAXB object to corresponding AST object. Special version to fix issue #924 with Calliope/Microbit <hide> problem
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> SensorMetaDataBean extractPortAndModeAndSlotForGyro(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.EMPTY_PORT);
        String modeName = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE, BlocklyConstants.DEFAULT);

        String robotGroup = helper.getRobotFactory().getGroup();
        boolean calliopeOrMicrobit = "calliope".equals(robotGroup) || "microbit".equals(robotGroup);
        String slotName;
        if ( calliopeOrMicrobit ) {
            slotName = Jaxb2Ast.extractNonEmptyField(fields, BlocklyConstants.SLOT, BlocklyConstants.X);
        } else {
            slotName = Jaxb2Ast.extractField(fields, BlocklyConstants.SLOT, BlocklyConstants.NO_SLOT);
        }
        return new SensorMetaDataBean(Jaxb2Ast.sanitizePort(portName), factory.getMode(modeName), Jaxb2Ast.sanitizeSlot(slotName), block.getMutation());
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        SensorMetaDataBean sensorMetaDataBean;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_GYRO_RESET) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorMetaDataBean =
                new SensorMetaDataBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("RESET"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return GyroSensor.make(sensorMetaDataBean, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        } else {
            sensorMetaDataBean = extractPortAndModeAndSlotForGyro(block, helper);
            return GyroSensor.make(sensorMetaDataBean, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        //TODO: move reset to another block and delete astToBlock() method from here
        String fieldValue = getUserDefinedPort();
        if ( getMode().equals("ANGLE") || getMode().equals("RATE") || getMode().equals("X") || getMode().equals("Y") || getMode().equals("Z") ) {
            Mutation mutation = new Mutation();
            mutation.setMode(getMode());
            jaxbDestination.setMutation(mutation);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, getMode());
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SLOT, getSlot());
        } else if ( getMode().equals("TILTED") ) {
            String fieldSlot = getSlot();
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SLOT, fieldSlot);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        } else {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        }
        return jaxbDestination;
    }

}
