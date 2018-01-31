package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class LedOnAction<V> extends Action<V> {
    private final Expr<V> ledColor;

    private LedOnAction(Expr<V> ledColor, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_ON_ACTION"), properties, comment);
        Assert.notNull(ledColor);
        this.ledColor = ledColor;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedOnAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedOnAction}
     */
    private static <V> LedOnAction<V> make(Expr<V> ledColor, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LedOnAction<>(ledColor, properties, comment);
    }

    /**
     * @return {@link ColorConst} color of the led.
     */
    public Expr<V> getLedColor() {
        return this.ledColor;
    }

    @Override
    public String toString() {
        return "LedOnAction [ " + this.ledColor + " ]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitLedOnAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);

        Phrase<V> ledColor = helper.extractValue(values, new ExprParam(BlocklyConstants.COLOR, BlocklyType.COLOR));

        return LedOnAction.make(helper.convertPhraseToExpr(ledColor), helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.COLOR, this.ledColor);

        return jaxbDestination;

    }
}
