package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "DATA_SEND_ACTION", category = "ACTOR", blocklyNames = {"robActions_sendData"})
public final class SendDataAction extends Action {

    public final List<Pair<String, Expr>> id2Phenomena;
    public final String destination;

    public SendDataAction(String destination, List<Pair<String, Expr>> id2Phenomena, BlocklyProperties properties) {
        super(properties);
        this.id2Phenomena = id2Phenomena;
        this.destination = destination;
        this.setReadOnly();
    }

    @Override
    public String toString() {
        return "DataSendAction []";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        ExprList exprList = helper.blockToExprList(block, BlocklyType.NUMBER);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 999);
        List<Pair<String, Expr>> id2Phenomena = new ArrayList<>();
        String destination = fields.get(0).getValue();
        for ( int i = 0; i < exprList.get().size(); i++ ) {
            id2Phenomena.add(Pair.of(fields.get(i + 1).getValue(), exprList.get().get(i)));
        }
        return new SendDataAction(destination, id2Phenomena, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        int numOfStrings = this.id2Phenomena.size();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfStrings));
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, "DESTINATION", this.destination);
        int i = 0;
        for ( Pair<String, Expr> kv : this.id2Phenomena ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PHEN + i, kv.getFirst());
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ADD + i, kv.getSecond());
            i++;
        }
        return jaxbDestination;
    }
}
