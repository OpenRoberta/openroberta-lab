package de.fhg.iais.roberta.syntax.action.ev3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.shared.action.ev3.BlinkMode;
import de.fhg.iais.roberta.shared.action.ev3.BrickLedColor;
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
public class LightAction<V> extends Action<V> {
    private final BrickLedColor color;
    private final BlinkMode blinkMode;

    private LightAction(BrickLedColor color, BlinkMode blinkMode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockType.LIGHT_ACTION, properties, comment);
        Assert.isTrue(color != null && blinkMode != null);
        this.color = color;
        this.blinkMode = blinkMode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightAction}. This instance is read only and
     * can not be modified.
     *
     * @param color of the lights on the brick. All possible colors are defined in {@link BrickLedColor}; must be <b>not</b> null,
     * @param blinkMode type of the blinking; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LightAction}
     */
    private static <V> LightAction<V> make(BrickLedColor color, BlinkMode blinkMode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LightAction<V>(color, blinkMode, properties, comment);
    }

    /**
     * @return {@link BrickLedColor} of the lights.
     */
    public BrickLedColor getColor() {
        return this.color;
    }

    /**
     * @return type of blinking.
     */
    public BlinkMode getBlinkMode() {
        return this.blinkMode;
    }

    @Override
    public String toString() {
        return "LightAction [" + this.color + ", " + this.blinkMode + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitLightAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String color = helper.extractField(fields, BlocklyConstants.SWITCH_COLOR);
        String blink = helper.extractField(fields, BlocklyConstants.SWITCH_BLINK);
        return LightAction.make(BrickLedColor.get(color), BlinkMode.get(blink), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SWITCH_COLOR, getColor().name());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SWITCH_BLINK, getBlinkMode().name());

        return jaxbDestination;

    }
}
