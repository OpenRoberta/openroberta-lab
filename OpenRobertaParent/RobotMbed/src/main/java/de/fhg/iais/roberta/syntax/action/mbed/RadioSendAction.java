package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

public class RadioSendAction<V> extends Action<V> {
    private final Expr<V> message;
    private final BlocklyType type;
    private final String power;

    private RadioSendAction(Expr<V> msg, BlocklyType type, String power, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RADIO_SEND_ACTION"), properties, comment);
        this.message = msg;
        this.type = type;
        this.power = power;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RadioSendAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RadioSendAction}
     */
    public static <V> RadioSendAction<V> make(Expr<V> expr, BlocklyType type, String power, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RadioSendAction<>(expr, type, power, properties, comment);
    }

    public Expr<V> getMsg() {
        return this.message;
    }

    public String getPower() {
        return this.power;
    }
    
    public BlocklyType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "RadioSendAction [ " + getMsg().toString() + ", " + getType().toString() + ", " + getPower() + " ]";
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
        List<Field> fields = helper.extractFields(block, (short) 2);
        Phrase<V> message = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.STRING));
        String power = helper.extractField(fields, BlocklyConstants.POWER);
        String type = helper.extractField(fields, BlocklyConstants.TYPE);
        
        return RadioSendAction.make(helper.convertPhraseToExpr(message), BlocklyType.get(type), power, helper.extractBlockProperties(block), helper.extractComment(block));
    }
    
    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDatatype(this.type.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.TYPE, this.type.getBlocklyName());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.MESSAGE, this.message);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.POWER, this.power);
        return jaxbDestination;
    }
}
