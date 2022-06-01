package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Frame;
import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>naoActions_rgbLeds</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * colouring the RGB-LEDs of NAO.<br/>
 * <br/>
 * The client must provide the {@link led}.
 */
public final class SetLeds<V> extends Action<V> {

    private final Led led;
    private final Expr<V> Color;

    private SetLeds(Led led, Expr<V> Color, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RGB_LED"), properties, comment);
        this.led = led;
        Assert.notNull(Color);
        this.Color = Color;
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
    private static <V> SetLeds<V> make(Led led, Expr<V> Color, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetLeds<V>(led, Color, properties, comment);
    }

    public Led getLed() {
        return this.led;
    }

    public Expr<V> getColor() {
        return this.Color;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        Phrase<V> Color = helper.extractValue(values, new ExprParam(BlocklyConstants.COLOR, BlocklyType.COLOR));

        String leds = Jaxb2Ast.extractField(fields, BlocklyConstants.LED);

        return SetLeds.make(Led.get(leds), Jaxb2Ast.convertPhraseToExpr(Color), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.COLOR, this.Color);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "SetLeds [" + this.led + ", " + this.Color + ", " + "]";
    }
}
