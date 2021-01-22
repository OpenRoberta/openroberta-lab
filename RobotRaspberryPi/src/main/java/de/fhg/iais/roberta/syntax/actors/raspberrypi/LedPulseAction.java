package de.fhg.iais.roberta.syntax.actors.raspberrypi;

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
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_ledBar_set</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for turning on the Grove LED Bar v2.0.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String, Expr, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class LedPulseAction<V> extends Action<V> {
    private final String port;
    private final Expr<V> fadeInTime;
    private final Expr<V> fadeOutTime;
    private final Expr<V> numBlinks;

    private LedPulseAction(String port, Expr<V> fadeInTime, Expr<V> fadeOutTime, Expr<V> numBlinks, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_PULSE_ACTION"), properties, comment);
        Assert.notNull(port);
        Assert.notNull(fadeInTime);
        Assert.notNull(fadeOutTime);
        Assert.notNull(numBlinks);
        this.port = port;
        this.fadeInTime = fadeInTime;
        this.fadeOutTime = fadeOutTime;
        this.numBlinks = numBlinks;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedPulseAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedPulseAction}
     */
    private static <V> LedPulseAction<V> make(
        String port,
        Expr<V> onTime,
        Expr<V> offTime,
        Expr<V> numBlinks,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new LedPulseAction<>(port, onTime, offTime, numBlinks, properties, comment);
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
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 3);

        Phrase<V> fadeInTime = helper.extractValue(values, new ExprParam(BlocklyConstants.FADE_IN_TIME, BlocklyType.NUMBER));
        Phrase<V> fadeOutTime = helper.extractValue(values, new ExprParam(BlocklyConstants.FADE_OUT_TIME, BlocklyType.NUMBER));
        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.N_TIMES, BlocklyType.NUMBER_INT));
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
        return LedPulseAction
            .make(
                port,
                Jaxb2Ast.convertPhraseToExpr(fadeInTime),
                Jaxb2Ast.convertPhraseToExpr(fadeOutTime),
                Jaxb2Ast.convertPhraseToExpr(duration),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));

    }

    /**
     * @return x of the pixel.
     */
    public String getPort() {
        return this.port;
    }

    public Expr<V> getFadeInTime() {
        return this.fadeInTime;
    }

    public Expr<V> getFadeOutTime() {
        return this.fadeOutTime;
    }

    public Expr<V> getNumBlinks() {
        return this.numBlinks;
    }

    @Override
    public String toString() {
        return "LedSetAction [ " + this.port + ", " + this.fadeInTime + ", " + this.fadeOutTime + ", " + this.numBlinks + " ]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FADE_IN_TIME, this.fadeInTime);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FADE_OUT_TIME, this.fadeOutTime);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.N_TIMES, this.numBlinks);
        return jaxbDestination;

    }
}
