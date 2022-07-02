package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoBasic(name = "ARDU_JOYSTICK_GETSAMPLE", category = "SENSOR", blocklyNames = {"robSensors_joystick_getSample"})
public final class Joystick<V> extends ExternalSensor<V> {
    public final String joystickAxis;

    public Joystick(String axis, ExternalSensorBean externalSensorBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment, externalSensorBean);
        this.joystickAxis = axis;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "Joystick [" + this.getMode() + ", " + this.getUserDefinedPort() + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);

        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
        String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
        ExternalSensorBean sensorData =
            new ExternalSensorBean(Jaxb2Ast.sanitizePort(port), factory.getMode(mode), Jaxb2Ast.sanitizeSlot(BlocklyConstants.EMPTY_SLOT), block.getMutation());
        return new Joystick<>(mode, sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        String fieldValue = this.getUserDefinedPort();
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        return jaxbDestination;
    }

}
