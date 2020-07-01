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
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

/**
 * This class represents the <b>mbedActions_fourdigitdisplay_clear</b> block from Blockly into the AST (abstract syntax tree). Object from this class will
 * generate code
 * for clearing the Grove 4-Digit Display.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 */
public class FourDigitDisplayShowAction<V> extends Action<V> {
    private final Expr<V> value;
    private final Expr<V> position;
    private final Expr<V> colon;

    private FourDigitDisplayShowAction(Expr<V> value, Expr<V> position, Expr<V> colon, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("FOURDIGITDISPLAY_SHOW_ACTION"), properties, comment);
        Assert.isTrue(value != null && position != null && colon != null);
        this.value = value;
        this.position = position;
        this.colon = colon;
        setReadOnly();
    }

    /**
     * Creates instance of {@link FourDigitDisplayShowAction}. This instance is read only and can not be modified.
     *
     * @param the value to display
     * @param the position to start displaying
     * @param whether to show a colon or not
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link FourDigitDisplayShowAction}
     */
    public static <V> FourDigitDisplayShowAction<V> make(
        Expr<V> value,
        Expr<V> position,
        Expr<V> colon,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new FourDigitDisplayShowAction<>(value, position, colon, properties, comment);
    }

    public Expr<V> getValue() {
        return this.value;
    }

    public Expr<V> getPosition() {
        return this.position;
    }

    public Expr<V> getColon() {
        return this.colon;
    }

    @Override
    public String toString() {
        return "FourDigitDisplayShowAction [" + this.value + ", " + this.position + ", " + this.colon + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitFourDigitDisplayShowAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 3);
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        Phrase<V> position = helper.extractValue(values, new ExprParam(BlocklyConstants.POSITION, BlocklyType.NUMBER_INT));
        Phrase<V> colon = helper.extractValue(values, new ExprParam(BlocklyConstants.COLON, BlocklyType.BOOLEAN));
        return FourDigitDisplayShowAction
            .make(
                helper.convertPhraseToExpr(value),
                helper.convertPhraseToExpr(position),
                helper.convertPhraseToExpr(colon),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.POSITION, this.position);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.COLON, this.colon);

        return jaxbDestination;
    }

}
