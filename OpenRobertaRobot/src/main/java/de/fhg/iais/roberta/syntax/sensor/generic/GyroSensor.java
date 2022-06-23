package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robSensors_gyro_getMode</b>, <b>robSensors_gyro_getSample</b> and <b>robSensors_gyro_setMode</b> blocks from Blockly into the
 * AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link GyroSensorMode}. See enum {@link GyroSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(GyroSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoBasic(sampleValues = {@F2M(field = "GYRO_TILTED", mode = "TILTED"), @F2M(field = "GYRO_Y", mode = "Y"), @F2M(field = "GYRO_RATE", mode = "RATE"), @F2M(field = "GYRO_Z", mode = "Z"), @F2M(field = "GYRO_X", mode = "X"), @F2M(field = "GYRO_ANGLE", mode = "ANGLE")}, name = "GYRO_SENSING", category = "SENSOR", blocklyNames = {"robSensors_gyro_getSample", "robSensors_gyro_reset", "mbedsensors_rotation_getsample"})
public final class GyroSensor<V> extends ExternalSensor<V> {

    public GyroSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        SensorMetaDataBean sensorMetaDataBean;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_GYRO_RESET) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorMetaDataBean =
                new SensorMetaDataBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("RESET"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return new GyroSensor<>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), sensorMetaDataBean);
        } else {
            sensorMetaDataBean = extractPortAndModeAndSlot(block, helper);
            return new GyroSensor<>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), sensorMetaDataBean);
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        //TODO: move reset to another block and delete astToBlock() method from here
        String fieldValue = getUserDefinedPort();
        String mode = getMode();
        if ( mode.equals("ANGLE") || mode.equals("RATE") || mode.equals("X") || mode.equals("Y") || mode.equals("Z") || mode.equals("TILTED") ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, mode);
            Mutation mutation = new Mutation();
            mutation.setMode(getMode());
            jaxbDestination.setMutation(mutation);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SLOT, getSlot());
        } else {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        }
        List<Hide> hide = getSensorMetaDataBean().getHide();
        if ( hide != null && hide.size() > 0 ) {
            jaxbDestination.getHide().addAll(hide);
        }
        return jaxbDestination;
    }

}
