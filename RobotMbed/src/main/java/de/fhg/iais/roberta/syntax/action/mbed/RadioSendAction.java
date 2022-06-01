package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        Phrase<V> message = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.STRING));
        String power = Jaxb2Ast.extractField(fields, BlocklyConstants.POWER);
        String type = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);

        return RadioSendAction
            .make(Jaxb2Ast.convertPhraseToExpr(message), BlocklyType.get(type), power, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDatatype(this.type.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.type.getBlocklyName());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.MESSAGE, this.message);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.POWER, this.power);
        return jaxbDestination;
    }
}
