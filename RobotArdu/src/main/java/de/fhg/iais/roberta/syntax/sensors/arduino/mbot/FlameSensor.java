package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "FLAMESENSOR_GET_SAMPLE", category = "SENSOR", blocklyNames = {"robSensors_flame_getSample"})
public final class FlameSensor extends Sensor {

    public final String port;

    public FlameSensor(String port, BlocklyProperties properties) {
        super(properties);
        this.port = port;
        setReadOnly();
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        final BlocklyDropdownFactory factory = helper.getDropdownFactory();
        final List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        final String port = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
        return new FlameSensor(Jaxb2Ast.sanitizePort(port), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        final Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        final String fieldValue = this.port;
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "flameSensor [port = " + this.port + "]";
    }

}
