package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "RADIO_SEND_ACTION", category = "ACTOR", blocklyNames = {"mbedCommunication_sendBlock"})
public final class RadioSendAction<V> extends Action<V> {
    public final Expr<V> message;
    public final BlocklyType type;
    public final String power;

    public RadioSendAction(Expr<V> msg, BlocklyType type, String power, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        this.message = msg;
        this.type = type;
        this.power = power;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "RadioSendAction [ " + this.message.toString() + ", " + this.type.toString() + ", " + this.power + " ]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        Phrase<V> message = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.STRING));
        String power = Jaxb2Ast.extractField(fields, BlocklyConstants.POWER);
        String type = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);

        return new RadioSendAction<>(Jaxb2Ast.convertPhraseToExpr(message), BlocklyType.get(type), power, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
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
