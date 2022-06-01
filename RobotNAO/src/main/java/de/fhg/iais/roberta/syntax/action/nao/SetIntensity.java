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
 * This class represents the <b>naoActions_setIntensity</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for setting the intensity of the NON-RGB-LEDs of NAO.<br/>
 * <br/>
 * The client must provide the {@link led}.
 */
public final class SetIntensity<V> extends Action<V> {

    private final Led led;
    private final Expr<V> Intensity;

    private SetIntensity(Led led, Expr<V> Intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_INTENSITY"), properties, comment);
        this.led = led;
        Assert.notNull(Intensity);
        this.Intensity = Intensity;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SetIntensity}. This instance is read only and can not be modified.
     *
     * @param frame {@link Frame} the coordinates relate to,
     * @param speed {@link speed} the movement will be executed at,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetIntensity}
     */
    private static <V> SetIntensity<V> make(Led led, Expr<V> Intensity, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetIntensity<V>(led, Intensity, properties, comment);
    }

    public Led getLed() {
        return this.led;
    }

    public Expr<V> getIntensity() {
        return this.Intensity;
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

        Phrase<V> Intensity = helper.extractValue(values, new ExprParam(BlocklyConstants.INTENSITY, BlocklyType.NUMBER_INT));

        String leds = Jaxb2Ast.extractField(fields, BlocklyConstants.LED);

        return SetIntensity.make(Led.get(leds), Jaxb2Ast.convertPhraseToExpr(Intensity), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.INTENSITY, this.Intensity);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "SetLeds [" + this.led + ", " + this.Intensity + ", " + "]";
    }
}
