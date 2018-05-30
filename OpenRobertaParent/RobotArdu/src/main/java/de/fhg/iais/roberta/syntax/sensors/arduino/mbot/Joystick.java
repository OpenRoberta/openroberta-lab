package de.fhg.iais.roberta.syntax.sensors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.mode.sensor.Axis;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.MbotAstVisitor;

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
        return "Joystick [" + this.getMode() + ", " + this.getPort() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbotAstVisitor<V>) visitor).visitJoystick(this);
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
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 2);

        String port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        String mode = helper.extractField(fields, BlocklyConstants.JOYSTICKAXIS);
        boolean isPortInMutation = block.getMutation() != null && block.getMutation().getPort() != null;
        SensorMetaDataBean sensorData =
            new SensorMetaDataBean(
                factory.getSensorPort(port),
                IRobotFactory.getModeValue(mode, Axis.class),
                factory.getSlot(BlocklyConstants.EMPTY_SLOT),
                isPortInMutation);
        return Joystick.make(mode, sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = this.getPort().getOraName();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        return jaxbDestination;
    }

}
