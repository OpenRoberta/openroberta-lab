package de.fhg.iais.roberta.syntax.sensor.makeblock;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.BaseSensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MakeblockAstVisitor;

public class Joystick<V> extends BaseSensor<V> {
    private final ISensorPort port;
    private final IJoystickMode mode;
    private final String joystickAxis;

    public Joystick(String axis, IJoystickMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(port, BlockTypeContainer.getByName("ARDU_JOYSTICK_GETSAMPLE"), properties, comment);
        this.port = port;
        this.mode = mode;
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
    public static <V> Joystick<V> make(String axis, IJoystickMode mode, ISensorPort port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Joystick<>(axis, mode, port, properties, comment);
    }

    @Override
    public ISensorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "Joystick [port = " + this.port + ", mode  = " + this.mode + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MakeblockAstVisitor<V>) visitor).visitJoystick(this);
    }

    public IJoystickMode getMode() {
        return this.mode;
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
        return Joystick
            .make(mode, factory.getJoystickMode(mode), factory.getSensorPort(port), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = this.port.getPortNumber();
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
        return jaxbDestination;
    }

}
