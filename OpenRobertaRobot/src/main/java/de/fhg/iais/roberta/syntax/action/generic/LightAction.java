package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
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
public class LightAction<V> extends Action<V> {
    private final IBrickLedColor color;
    private final IBlinkMode blinkMode;
    private static List<Field> fields;

    private LightAction(IBrickLedColor color, IBlinkMode blinkMode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIGHT_ACTION"),properties, comment);
        Assert.isTrue(color != null && blinkMode != null);
        this.color = color;
        this.blinkMode = blinkMode;
        setReadOnly();
    }

    private LightAction(IBlinkMode blinkMode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIGHT_ACTION"),properties, comment);
        Assert.isTrue(blinkMode != null);
        this.color = null;
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
    private static <V> LightAction<V> make(IBrickLedColor color, IBlinkMode blinkMode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LightAction<V>(color, blinkMode, properties, comment);
    }

    private static <V> LightAction<V> make(IBlinkMode blinkMode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LightAction<V>(blinkMode, properties, comment);
    }

    /**
     * @return {@link BrickLedColor} of the lights.
     */
    public IBrickLedColor getColor() {
        return this.color;
    }

    /**
     * @return type of blinking.
     */
    public IBlinkMode getBlinkMode() {
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
        IRobotFactory factory = helper.getModeFactory();
        fields = helper.extractFields(block, (short) 2);
        String blink = helper.extractField(fields, BlocklyConstants.SWITCH_BLINK);
        if ( fields.size() != 1 ) {
            String color = helper.extractField(fields, BlocklyConstants.SWITCH_COLOR);
            return LightAction
                .make(factory.getBrickLedColor(color), factory.getBlinkMode(blink), helper.extractBlockProperties(block), helper.extractComment(block));
        } else {
            return LightAction.make(factory.getBlinkMode(blink), helper.extractBlockProperties(block), helper.extractComment(block));
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        if ( fields.size() != 1 ) {
            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SWITCH_COLOR, getColor().toString());
        }
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SWITCH_BLINK, getBlinkMode().toString());

        return jaxbDestination;

    }
}
