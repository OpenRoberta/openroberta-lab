package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.math.BigInteger;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class SendDataAction<V> extends Action<V> {

    private final ExprList<V> listOfSensors;

    private SendDataAction(ExprList<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DATA_SEND_ACTION"), properties, comment);
        this.listOfSensors = param;
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link SendDataAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SendDataAction}
     */
    public static <V> SendDataAction<V> make(ExprList<V> param, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SendDataAction<>(param, properties, comment);
    }

    @Override
    public String toString() {
        return "DataSendAction []";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitDataSendAction(this);
    }

    public ExprList<V> getParam() {
        return listOfSensors;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        ExprList<V> exprList = helper.blockToExprList(block, BlocklyType.STRING);
        return SendDataAction.make(exprList, helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        int numOfStrings = getParam().get().size();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfStrings));
        jaxbDestination.setMutation(mutation);
        for ( int i = 0; i < numOfStrings; i++ ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.ADD + i, getParam().get().get(i));
        }
        return jaxbDestination;
    }
}
