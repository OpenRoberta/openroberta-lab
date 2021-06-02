package de.fhg.iais.roberta.syntax.action.raspberrypi;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_ledBar_set</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for turning on the Grove LED Bar v2.0.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class LedBlinkAction<V> extends Action<V> {
    private final String port;
    private final Expr<V> frequency;
    private final Expr<V> duration;

    private LedBlinkAction(String port, Expr<V> frequency, Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_BLINK_ACTION"), properties, comment);
        Assert.notNull(port);
        Assert.notNull(frequency);
        Assert.notNull(duration);
        this.port = port;
        this.frequency = frequency;
        this.duration = duration;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedBlinkAction}. This instance is read only and can not be modified.
     *
     * @param brightness of the display; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedBlinkAction}
     */
    private static <V> LedBlinkAction<V> make(String port, Expr<V> frequency, Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LedBlinkAction<>(port, frequency, duration, properties, comment);
    }

    /**
     * @return x of the pixel.
     */
    public String getPort() {
        return this.port;
    }

    public Expr<V> getFrequency() {
        return this.frequency;
    }

    public Expr<V> getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return "LedSetAction [ " + this.port + ", " + this.frequency + ", " + this.duration + " ]";
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
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);

        Phrase<V> frequency = helper.extractValue(values, new ExprParam(BlocklyConstants.FREQUENCY, BlocklyType.NUMBER));
        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, BlocklyType.NUMBER_INT));
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
        return LedBlinkAction
            .make(
                port,
                Jaxb2Ast.convertPhraseToExpr(frequency),
                Jaxb2Ast.convertPhraseToExpr(duration),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FREQUENCY, this.frequency);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.DURATION, this.duration);
        return jaxbDestination;

    }
}
