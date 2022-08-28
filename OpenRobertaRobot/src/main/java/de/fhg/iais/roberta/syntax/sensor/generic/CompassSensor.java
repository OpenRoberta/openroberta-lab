package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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

@NepoBasic(sampleValues = {@F2M(field = "COMPASS_COMPASS", mode = "COMPASS"), @F2M(field = "COMPASS_X", mode = "X"), @F2M(field = "COMPASS_Y", mode = "Y"), @F2M(field = "COMPASS_ANGLE", mode = "ANGLE"), @F2M(field = "COMPASS_Z", mode = "Z")}, name = "COMPASS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_compass_getSample", "mbedsensors_compass_getsample", "robSensors_compass_calibrate"})
public final class CompassSensor extends ExternalSensor {

    public CompassSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        ExternalSensorBean externalSensorBean;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_COMPASS_CALIBRATE) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            externalSensorBean =
                new ExternalSensorBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("CALIBRATE"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return new CompassSensor(Jaxb2Ast.extractBlocklyProperties(block), externalSensorBean);
        }
        ExternalSensorBean sensorData = extractPortAndModeAndSlot(block, helper);
        return new CompassSensor(Jaxb2Ast.extractBlocklyProperties(block), sensorData);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = super.astToBlock();
        if ( getMode().toString().equals("CALIBRATE") ) {
            jaxbDestination = new Block();
            Ast2Jaxb.setBasicProperties(this, jaxbDestination);
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, getUserDefinedPort());
        }
        return jaxbDestination;
    }
}
