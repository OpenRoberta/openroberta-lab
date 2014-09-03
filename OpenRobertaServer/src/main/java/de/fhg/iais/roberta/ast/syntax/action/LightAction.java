package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_brickLight_on</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning the light on.<br/>
 * <br/>
 * The client must provide the {@link BrickLedColor} of the lights and the mode of blinking.
 */
public class LightAction<V> extends Action<V> {
    private final BrickLedColor color;
    private final boolean blink;

    private LightAction(BrickLedColor color, boolean blink, boolean disabled, String comment) {
        super(Phrase.Kind.LIGHT_ACTION, disabled, comment);
        Assert.isTrue(color != null);
        this.color = color;
        this.blink = blink;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightAction}. This instance is read only and can not be modified.
     * 
     * @param color of the lights on the brick. All possible colors are defined in {@link BrickLedColor},
     * @param blink type of the blinking,
     * @param disabled state of the block,
     * @param comment added from the user
     * @return read only object of class {@link LightAction}.
     */
    public static <V> LightAction<V> make(BrickLedColor color, boolean blink, boolean disabled, String comment) {
        return new LightAction<V>(color, blink, disabled, comment);
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
    public boolean isBlink() {
        return this.blink;
    }

    @Override
    public String toString() {
        return "LightAction [" + this.color + ", " + this.blink + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitLightAction(this);
    }
}
