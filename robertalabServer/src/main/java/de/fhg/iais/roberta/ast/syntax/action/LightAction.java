package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_brickLight_on</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning the light on.<br/>
 * <br/>
 * The client must provide the {@link Color} of the lights and the mode of blinking.
 */
public class LightAction extends Action {
    private final Color color;
    private final boolean blink;

    private LightAction(Color color, boolean blink) {
        super(Phrase.Kind.LIGHT_ACTION);
        Assert.isTrue(color != null);
        this.color = color;
        this.blink = blink;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightAction}. This instance is read only and can not be modified.
     * 
     * @param color of the lights on the brick. All possible colors are defined in {@link Color},
     * @param blink type of the blinking,
     * @return read only object of class {@link LightAction}.
     */
    public static LightAction make(Color color, boolean blink) {
        return new LightAction(color, blink);
    }

    /**
     * @return {@link Color} of the lights.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * @return type of blinking.
     */
    public boolean isBlink() {
        return this.blink;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("hal.ledOn(" + this.color.toString() + ", " + this.blink + ");");
    }

    @Override
    public String toString() {
        return "LightAction [" + this.color + ", " + this.blink + "]";
    }
}
