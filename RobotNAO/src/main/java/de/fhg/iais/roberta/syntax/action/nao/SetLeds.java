package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Color;
import de.fhg.iais.roberta.mode.action.nao.Frame;
import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_pointAt</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for making the NAO point at a specific position.<br/>
 * <br/>
 * The client must provide the {@link Frame}, {@link pointX}, {@link pointY}, {@link pointZ} and {@link speed} (frame, coordinates and fraction of speed).
 */
public final class SetLeds<V> extends Action<V> {

    private final Led led;
    private final Color color;
    private final Expr<V> intensity;

    private SetLeds(Led led, Color color, Expr<V> intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_LEDS"), properties, comment);
        //Assert.notNull(frame, "Missing frame in PointLookAt block!");
        //Assert.notNull(pointLook, "Missing point look in PointLookAt block!");
        this.led = led;
        this.color = color;
        this.intensity = intensity;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SetLeds}. This instance is read only and can not be modified.
     *
     * @param frame {@link Frame} the coordinates relate to,
     * @param speed {@link speed} the movement will be executed at,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetLeds}
     */
    private static <V> SetLeds<V> make(Led led, Color color, Expr<V> intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetLeds<V>(led, color, intensity, properties, comment);
    }

    public Led getLed() {
        return this.led;
    }

    public Color getColor() {
        return this.color;
    }

    public Expr<V> getIntensity() {
        return this.intensity;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitSetLeds(this);
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
        List<Value> values = helper.extractValues(block, (short) 1);

        String leds = helper.extractField(fields, BlocklyConstants.LED);
        String color = helper.extractField(fields, BlocklyConstants.COLOR);
        Phrase<V> intensity = helper.extractValue(values, new ExprParam(BlocklyConstants.INTENSITY, Integer.class));

        return SetLeds
            .make(Led.get(leds), Color.get(color), helper.convertPhraseToExpr(intensity), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.COLOR, this.color.toString());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.INTENSITY, this.intensity);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "SetLeds [" + this.led + ", " + this.color + ", " + this.intensity + "]";
    }
}
