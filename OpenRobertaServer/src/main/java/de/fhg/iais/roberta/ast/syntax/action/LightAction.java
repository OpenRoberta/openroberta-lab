package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

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
        super(Phrase.Kind.LIGHT_ACTION, properties, comment);
        Assert.isTrue(color != null);
        this.color = color;
        this.blinkMode = blinkMode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightAction}. This instance is read only and
     * can not be modified.
     *
     * @param color of the lights on the brick. All possible colors are defined in {@link BrickLedColor},
     * @param blinkMode type of the blinking,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LightAction}.
     */
    public static <V> LightAction<V> make(BrickLedColor color, BlinkMode blinkMode, BlocklyBlockProperties properties, BlocklyComment comment) {
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

    public static enum BlinkMode {
        ON(), FLASH(), DOUBLE_FLASH();

        private final String[] values;

        private BlinkMode(String... values) {
            this.values = values;
        }

        /**
         * @return valid Java code name of the enumeration
         */
        public String getJavaCode() {
            return this.getClass().getSimpleName() + "." + this;
        }

        /**
         * get mode from {@link BlinkMode} from string parameter. It is possible
         * for one mode to have multiple string mappings. Throws exception if
         * the mode does not exists.
         *
         * @param name of the mode
         * @return mode from the enum {@link BlinkMode}
         */
        public static BlinkMode get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid mode: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( BlinkMode mo : BlinkMode.values() ) {
                if ( mo.toString().equals(sUpper) ) {
                    return mo;
                }
                for ( String value : mo.values ) {
                    if ( sUpper.equals(value) ) {
                        return mo;
                    }
                }
            }
            throw new DbcException("Invalid mode: " + s);
        }
    }
}
