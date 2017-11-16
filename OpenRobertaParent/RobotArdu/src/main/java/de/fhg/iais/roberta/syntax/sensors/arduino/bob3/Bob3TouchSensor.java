package de.fhg.iais.roberta.syntax.sensors.arduino.bob3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.Bob3AstVisitor;

/**
 * This class represents the <b>robSensors_touch_isPressed</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for checking if the sensor is pressed.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class Bob3TouchSensor<V> extends Sensor<V> {
    private final String armSide;
    private final String armPart;

    private Bob3TouchSensor(String armSide, String armPart, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOB3_ARMS"), properties, comment);
        this.armPart = armPart;
        this.armSide = armSide;
        setReadOnly();
    }

    /**
     * Create object of the class {@link Bob3TouchSensor}.
     *
     * @param port on which the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible ports that the sensor can be
     *        connected,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link Bob3TouchSensor}
     */
    public static <V> Bob3TouchSensor<V> make(String armSide, String armPart, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Bob3TouchSensor<V>(armSide, armPart, properties, comment);
    }

    public String getArmSide() {
        return this.armSide;
    }

    public String getArmPart() {
        return this.armPart;
    }

    @Override
    public String toString() {
        return "TouchSensor []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((Bob3AstVisitor<V>) visitor).visitTouchSensor(this);
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
        String armSide = helper.extractField(fields, BlocklyConstants.ARMSIDE);
        String armPart = helper.extractField(fields, BlocklyConstants.ARMPART);
        return Bob3TouchSensor.make(armSide, armPart, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.ARMSIDE, getArmSide());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.ARMPART, getArmPart());
        return jaxbDestination;
    }
}
