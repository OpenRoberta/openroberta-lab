package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
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
    private final IWorkingState state;
    private final ISensorPort port;

    private LightSensorAction(ISensorPort port, ILightSensorActionMode light, IWorkingState state, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIGHT_SENSOR_ACTION"),properties, comment);
        Assert.isTrue(light != null);
        this.light = light;
        this.state = state;
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightSensorAction}. This instance is read only and
     * can not be modified.
     *
     * @param iActorPort
     * @param color of the lights on the sensor. All possible colors are defined in {@link BrickLedColor}; must be <b>not</b> null,
     * @param blinkMode type of the blinking; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LightSensorAction}
     */
    public static <V> LightSensorAction<V> make(
        ISensorPort port,
        ILightSensorActionMode light,
        IWorkingState state,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new LightSensorAction<V>(port, light, state, properties, comment);
    }

    /**
     * @return {@link ILightSensorActionMode} color of the light.
     */
    public ILightSensorActionMode getLight() {
        return light;
    }

    public IWorkingState getState() {
        return state;
    }

    public ISensorPort getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "LightSensorAction [" + light + ", " + state + ", " + port + "]";
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
        String state;
        IRobotFactory factory = helper.getModeFactory();
        fields = block.getField();
        light = helper.extractField(fields, BlocklyConstants.SWITCH_COLOR);
        state = helper.extractField(fields, BlocklyConstants.SWITCH_STATE);
        port = helper.extractField(fields, BlocklyConstants.SENSORPORT);

        return LightSensorAction.make(
            factory.getSensorPort(port),
            factory.getLightActionColor(light),
            factory.getWorkingState(state),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SWITCH_COLOR, getLight().toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SWITCH_STATE, getState().toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, getPort().getPortNumber());

        return jaxbDestination;

    }
}
