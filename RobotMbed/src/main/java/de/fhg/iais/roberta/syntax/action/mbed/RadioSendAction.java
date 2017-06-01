package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MbedAstVisitor;

public class RadioSendAction<V> extends Action<V> {
    private final Expr<V> message;

    private RadioSendAction(Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RADIO_SEND_ACTION"), properties, comment);
        this.message = msg;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RadioSendAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RadioSendAction}
     */
    public static <V> RadioSendAction<V> make(Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RadioSendAction<>(msg, properties, comment);
    }

    public Expr<V> getMsg() {
        return this.message;
    }

    @Override
    public String toString() {
        return "RadioSendAction [ " + getMsg().toString() + " ]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitRadioSendAction(this);
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
        Phrase<V> message = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.STRING));

        return RadioSendAction.make(helper.convertPhraseToExpr(message), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDatatype("String");
        jaxbDestination.setMutation(mutation);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.TYPE, "String");
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.MESSAGE, this.message);
        return jaxbDestination;
    }

}
