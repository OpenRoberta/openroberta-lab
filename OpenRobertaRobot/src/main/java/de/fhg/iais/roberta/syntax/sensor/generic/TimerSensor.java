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

@NepoBasic(sampleValues = {@F2M(field = "TIMER_VALUE", mode = "VALUE"), @F2M(field = "TIME", mode = "VALUE")}, name = "TIMER_SENSING", category = "SENSOR", blocklyNames = {"mbedSensors_timer_reset", "robSensors_timer_getSample", "robSensors_timer_reset"})
public final class TimerSensor extends ExternalSensor {

    public TimerSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        ExternalSensorBean externalSensorBean;
        //TODO This if statement should be removed when we have new implementation of reset sensor blockly block
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_TIMER_RESET) || block.getType().equals(BlocklyConstants.ROB_SENSORS_TIMER_RESET_CALLIOPE) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            externalSensorBean =
                new ExternalSensorBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("RESET"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return new TimerSensor(Jaxb2Ast.extractBlocklyProperties(block), externalSensorBean);
        }
        externalSensorBean = extractPortAndModeAndSlot(block, helper);
        return new TimerSensor(Jaxb2Ast.extractBlocklyProperties(block), externalSensorBean);
    }

    @Override
    public Block astToBlock() {
        if ( getMode().toString().equals("RESET") ) {
            Block jaxbDestination = new Block();
            Ast2Jaxb.setBasicProperties(this, jaxbDestination);
            String fieldValue = getUserDefinedPort();
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
            return jaxbDestination;
        } else {
            return super.astToBlock();
        }

    }

}
