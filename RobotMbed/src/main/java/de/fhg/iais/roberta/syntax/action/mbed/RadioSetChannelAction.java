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
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

public class RadioSetChannelAction<V> extends Action<V> {
    private final Expr<V> channel;

    private RadioSetChannelAction(Expr<V> channel, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RADIO_SET_CHANNEL_ACTION"), properties, comment);
        this.channel = channel;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RadioSetChannelAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RadioSetChannelAction}
     */
    public static <V> RadioSetChannelAction<V> make(Expr<V> channel, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RadioSetChannelAction<>(channel, properties, comment);
    }

    public Expr<V> getChannel() {
        return this.channel;
    }

    @Override
    public String toString() {
        return "RadioSetChannelAction [ " + getChannel().toString() + " ]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitRadioSetChannelAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> channel = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, BlocklyType.NUMBER_INT));

        return RadioSetChannelAction.make(helper.convertPhraseToExpr(channel), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, this.channel);
        return jaxbDestination;
    }

}
