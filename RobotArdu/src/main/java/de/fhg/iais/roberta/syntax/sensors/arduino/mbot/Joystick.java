package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.util.syntax.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

public class Joystick<V> extends ExternalSensor<V> {
    private final String joystickAxis;

    public Joystick(String axis, SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("ARDU_JOYSTICK_GETSAMPLE"), properties, comment);
        this.joystickAxis = axis;
        setReadOnly();
    }

    /**
     * Create object of the class {@link Joystick}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link Joystick}
     */
    public static <V> Joystick<V> make(String axis, SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Joystick<>(axis, sensorMetaDataBean, properties, comment);
    }

    @Override
    public String toString() {
        return "Joystick [" + this.getMode() + ", " + this.getUserDefinedPort() + "]";
    }

    public String getAxis() {
        return this.joystickAxis;
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
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);

        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
        String mode = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
        SensorMetaDataBean sensorData =
            new SensorMetaDataBean(Jaxb2Ast.sanitizePort(port), factory.getMode(mode), Jaxb2Ast.sanitizeSlot(BlocklyConstants.EMPTY_SLOT), block.getMutation());
        return Joystick.make(mode, sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
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
