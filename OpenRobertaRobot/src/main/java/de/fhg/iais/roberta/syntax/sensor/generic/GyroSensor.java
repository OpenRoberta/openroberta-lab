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
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(sampleValues = {@F2M(field = "GYRO_TILTED", mode = "TILTED"), @F2M(field = "GYRO_Y", mode = "Y"), @F2M(field = "GYRO_RATE", mode = "RATE"), @F2M(field = "GYRO_Z", mode = "Z"), @F2M(field = "GYRO_X", mode = "X"), @F2M(field = "GYRO_ANGLE", mode = "ANGLE")}, name = "GYRO_SENSING", category = "SENSOR", blocklyNames = {"robSensors_gyro_getSample", "robSensors_gyro_reset", "mbedsensors_rotation_getsample"})
public final class GyroSensor extends ExternalSensor {

    public GyroSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        ExternalSensorBean externalSensorBean;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_GYRO_RESET) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            externalSensorBean =
                new ExternalSensorBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("RESET"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return new GyroSensor(Jaxb2Ast.extractBlocklyProperties(block), externalSensorBean);
        } else {
            externalSensorBean = extractPortAndModeAndSlot(block, helper);
            return new GyroSensor(Jaxb2Ast.extractBlocklyProperties(block), externalSensorBean);
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
