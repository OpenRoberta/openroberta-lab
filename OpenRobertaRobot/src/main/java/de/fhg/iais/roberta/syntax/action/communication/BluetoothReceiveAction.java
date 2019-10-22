package de.fhg.iais.roberta.syntax.action.communication;

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
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IBluetoothVisitor;

public class BluetoothReceiveAction<V> extends Action<V> {
    private final Expr<V> connection;
    String channel;
    String dataType;

    private BluetoothReceiveAction(
        Expr<V> bluetoothRecieveConnection,
        String channel,
        String dataType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BLUETOOTH_RECEIVED_ACTION"), properties, comment);
        this.connection = bluetoothRecieveConnection;
        this.channel = channel;
        this.dataType = dataType;
        setReadOnly();
    }

    public static <V> BluetoothReceiveAction<V> make(
        Expr<V> bluetoothRecieveConnection,
        String channel,
        String dataType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new BluetoothReceiveAction<V>(bluetoothRecieveConnection, channel, dataType, properties, comment);
    }

    public Expr<V> getConnection() {
        return this.connection;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getDataType() {
        return this.dataType;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IBluetoothVisitor<V>) visitor).visitBluetoothReceiveAction(this);
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
        List<Field> fields = helper.extractFields(block, (short) 3);
        Phrase<V> bluetoothRecieveConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, BlocklyType.NULL));
        if ( fields.size() == 3 ) {
            String bluetoothRecieveChannel = helper.extractField(fields, BlocklyConstants.CHANNEL);
            String bluetoothRecieveDataType = helper.extractField(fields, BlocklyConstants.TYPE);
            return BluetoothReceiveAction
                .make(
                    helper.convertPhraseToExpr(bluetoothRecieveConnection),
                    bluetoothRecieveChannel,
                    bluetoothRecieveDataType,
                    helper.extractBlockProperties(block),
                    helper.extractComment(block));
        } else {
            String bluetoothReceiveChannel = "-1";
            String bluetoothRecieveDataType = helper.extractField(fields, BlocklyConstants.TYPE);
            return BluetoothReceiveAction
                .make(
                    helper.convertPhraseToExpr(bluetoothRecieveConnection),
                    bluetoothReceiveChannel,
                    bluetoothRecieveDataType,
                    helper.extractBlockProperties(block),
                    helper.extractComment(block));
        }
    }

    //TODO: add tests for NXT blocks
    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        Mutation mutation = new Mutation();
        mutation.setDatatype(this.dataType);
        jaxbDestination.setMutation(mutation);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TYPE, this.dataType);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.PROTOCOL, "BLUETOOTH");
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.CHANNEL, getChannel());

        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "BluetoothReceiveAction [connection=" + this.connection + ", " + this.channel + ", " + this.dataType + "]";
    }

}
