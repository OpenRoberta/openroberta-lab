package de.fhg.iais.roberta.syntax.actors.raspberrypi;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.*;
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
public class LedBlinkAction<V> extends Action<V> {
    private final String port;
    private final Expr<V> onTime;
    private final Expr<V> offTime;
    private final Expr<V> numBlinks;

    private LedBlinkAction(String port, Expr<V> onTime, Expr<V> offTime, Expr<V> numBlinks, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_BLINK_ACTION"), properties, comment);
        Assert.notNull(port);
        Assert.notNull(onTime);
        Assert.notNull(offTime);
        Assert.notNull(numBlinks);
        this.port = port;
        this.onTime = onTime;
        this.offTime = offTime;
        this.numBlinks = numBlinks;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedBlinkAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedBlinkAction}
     */
    private static <V> LedBlinkAction<V> make(
        String port,
        Expr<V> onTime,
        Expr<V> offTime,
        Expr<V> numBlinks,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new LedBlinkAction<>(port, onTime, offTime, numBlinks, properties, comment);
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

        Phrase<V> onTime = helper.extractValue(values, new ExprParam(BlocklyConstants.ON_TIME, BlocklyType.NUMBER));
        Phrase<V> offTime = helper.extractValue(values, new ExprParam(BlocklyConstants.OFF_TIME, BlocklyType.NUMBER));
        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.N_TIMES, BlocklyType.NUMBER_INT));
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
        return LedBlinkAction
            .make(
                port,
                Jaxb2Ast.convertPhraseToExpr(onTime),
                Jaxb2Ast.convertPhraseToExpr(offTime),
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

    public Expr<V> getOnTime() {
        return this.onTime;
    }

    public Expr<V> getOffTime() {
        return this.offTime;
    }

    public Expr<V> getNumBlinks() {
        return this.numBlinks;
    }

    @Override
    public String toString() {
        return "LedSetAction [ " + this.port + ", " + this.onTime + ", " + this.offTime + ", " + this.numBlinks + " ]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ON_TIME, this.onTime);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.OFF_TIME, this.offTime);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.N_TIMES, this.numBlinks);
        return jaxbDestination;

    }
}
