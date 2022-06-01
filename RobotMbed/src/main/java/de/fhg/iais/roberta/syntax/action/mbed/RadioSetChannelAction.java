package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase<V> channel = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, BlocklyType.NUMBER_INT));

        return RadioSetChannelAction.make(Jaxb2Ast.convertPhraseToExpr(channel), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.CONNECTION, this.channel);
        return jaxbDestination;
    }

}
