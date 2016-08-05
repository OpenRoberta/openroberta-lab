package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robActions_brickLight_on</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class will generate
 * code for turning the light on.<br/>
 * <br/>
 * The client must provide the {@link BrickLedColor} of the lights and the mode
 * of blinking.
 */
public class LightSensorAction<V> extends Action<V> {
    private final ILightSensorActionMode light;

    private LightSensorAction(ISensorPort port, ILightSensorActionMode light, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockType.LIGHT_SENSOR_ACTION, properties, comment);
        Assert.isTrue(light != null);
        this.light = light;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightSensorAction}. This instance is read only and
     * can not be modified.
     *
     * @param iActorPort
     * @param color of the lights on the brick. All possible colors are defined in {@link BrickLedColor}; must be <b>not</b> null,
     * @param blinkMode type of the blinking; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LightSensorAction}
     */
    private static <V> LightSensorAction<V> make(ISensorPort port, ILightSensorActionMode light, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LightSensorAction<V>(port, light, properties, comment);
    }

    /**
     * @return {@link BrickLedColor} of the lights.
     */
    public ILightSensorActionMode getLight() {
        return light;
    }

    /**
     * @return type of blinking.
     */

    @Override
    public String toString() {
        return "LightSensorAction [" + light + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitLightSensorAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        String port;
        List<Field> fields;
        String light;
        IRobotFactory factory = helper.getModeFactory();
        if ( block.getType().equals(BlocklyConstants.SENSOR_LIGHT_ON) ) {
            fields = helper.extractFields(block, (short) 1);
            port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
            light = helper.extractField(fields, BlocklyConstants.SWITCH_COLOR);
        } else {
            fields = helper.extractFields(block, (short) 3);
            port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
            light = helper.extractField(fields, BlocklyConstants.SWITCH_COLOR);
        }
        return LightSensorAction
            .make(factory.getSensorPort(port), factory.getLightActionColor(light), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SWITCH_COLOR, getLight().toString());

        return jaxbDestination;

    }
}
