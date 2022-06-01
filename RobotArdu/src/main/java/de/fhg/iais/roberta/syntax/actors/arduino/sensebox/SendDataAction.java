package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.basic.Pair;

public class SendDataAction<V> extends Action<V> {

    private final List<Pair<String, Expr<V>>> id2Phenomena;
    private final String destination;

    private SendDataAction(String destination, List<Pair<String, Expr<V>>> id2Phenomena, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DATA_SEND_ACTION"), properties, comment);
        this.id2Phenomena = id2Phenomena;
        this.destination = destination;
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link SendDataAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SendDataAction}
     */
    public static <V> SendDataAction<V> make(
        String destination,
        List<Pair<String, Expr<V>>> id2Phenomena,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new SendDataAction<>(destination, id2Phenomena, properties, comment);
    }

    @Override
    public String toString() {
        return "DataSendAction []";
    }

    public List<Pair<String, Expr<V>>> getId2Phenomena() {
        return this.id2Phenomena;
    }

    public String getDestination() {
        return this.destination;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        ExprList<V> exprList = helper.blockToExprList(block, BlocklyType.NUMBER);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 999);
        List<Pair<String, Expr<V>>> id2Phenomena = new ArrayList<>();
        String destination = fields.get(0).getValue();
        for ( int i = 0; i < exprList.get().size(); i++ ) {
            id2Phenomena.add(Pair.of(fields.get(i + 1).getValue(), exprList.get().get(i)));
        }
        return SendDataAction.make(destination, id2Phenomena, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        int numOfStrings = getId2Phenomena().size();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfStrings));
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, "DESTINATION", this.destination);
        int i = 0;
        for ( Pair<String, Expr<V>> kv : getId2Phenomena() ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PHEN + i, kv.getFirst());
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ADD + i, kv.getSecond());
            i++;
        }
        return jaxbDestination;
    }
}
