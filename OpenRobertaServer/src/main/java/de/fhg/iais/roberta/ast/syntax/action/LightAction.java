package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_brickLight_on</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning the light on.<br/>
 * <br/>
 * The client must provide the {@link BrickLedColor} of the lights and the mode of blinking.
 */
public class LightAction extends Action {
    private final BrickLedColor color;
    private final boolean blink;

    private LightAction(BrickLedColor color, boolean blink) {
        super(Phrase.Kind.LIGHT_ACTION);
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
     * @return read only object of class {@link LightAction}.
     */
    public static LightAction make(BrickLedColor color, boolean blink) {
        return new LightAction(color, blink);
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
    public void accept(Visitor visitor) {
        visitor.visit(this);

    }
}
