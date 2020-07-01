package de.fhg.iais.roberta.syntax.action.communication;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
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

public class BluetoothSendAction<V> extends Action<V> {
    private final Expr<V> _connection;
    private final Expr<V> _msg;
    String _channel;
    String dataType;
    private final String dataValue;

    private BluetoothSendAction(
        String dataValue,
        Expr<V> connection,
        Expr<V> msg,
        String channel,
        String dataType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BLUETOOTH_SEND_ACTION"), properties, comment);
        this._connection = connection;
        this._msg = msg;
        this._channel = channel;
        this.dataType = dataType;
        this.dataValue = dataValue;
        setReadOnly();
    }

    private BluetoothSendAction(String dataValue, Expr<V> connection, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BLUETOOTH_SEND_ACTION"), properties, comment);
        this._connection = connection;
        this._msg = msg;
        this.dataValue = dataValue;
        setReadOnly();
    }

    /**
     * Creates instance of {@link BluetoothSendAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BluetoothSendAction}
     */
    public static <V> BluetoothSendAction<V> make(
        String dataValue,
        Expr<V> connection,
        Expr<V> msg,
        String channel,
        String dataType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new BluetoothSendAction<>(dataValue, connection, msg, channel, dataType, properties, comment);
    }

    public static <V> BluetoothSendAction<V> make(
        String dataValue,
        Expr<V> connection,
        Expr<V> msg,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new BluetoothSendAction<>(dataValue, connection, msg, properties, comment);
    }

    public String getDataValue() {
        return this.dataValue;
    }

    public Expr<V> getConnection() {
        return this._connection;
    }

    public Expr<V> getMsg() {
        return this._msg;
    }

    public String getChannel() {
        return this._channel;
    }

    public String getDataType() {
        return this.dataType;
    }

    @Override
    public String toString() {
        return "BluetoothSendAction [" + getConnection().toString() + ", " + getMsg().toString() + ", " + getChannel() + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IBluetoothVisitor<V>) visitor).visitBluetoothSendAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 2);
        List<Field> fields = helper.extractFields(block, (short) 3);
        Phrase<V> bluetoothSendMessage = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.STRING));
        Phrase<V> bluetoothSendConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, BlocklyType.NULL));
        Data data = block.getData();
        String datum = data.getValue();
        if ( fields.size() == 3 ) {
            String bluetoothSendChannel = helper.extractField(fields, BlocklyConstants.CHANNEL);
            String bluetoothRecieveDataType = helper.extractField(fields, BlocklyConstants.TYPE);
            return BluetoothSendAction
                .make(
                    datum,
                    helper.convertPhraseToExpr(bluetoothSendConnection),
                    helper.convertPhraseToExpr(bluetoothSendMessage),
                    bluetoothSendChannel,
                    bluetoothRecieveDataType,
                    helper.extractBlockProperties(block),
                    helper.extractComment(block));
        } else {
            String bluetoothSendChannel = "-1";
            String bluetoothRecieveDataType = helper.extractField(fields, BlocklyConstants.TYPE);
            return BluetoothSendAction
                .make(
                    datum,
                    helper.convertPhraseToExpr(bluetoothSendConnection),
                    helper.convertPhraseToExpr(bluetoothSendMessage),
                    bluetoothSendChannel,
                    bluetoothRecieveDataType,
                    helper.extractBlockProperties(block),
                    helper.extractComment(block));
        }
    }

    //TODO: fix tests for NXT
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
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.MESSAGE, getMsg());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());
        Data data = new Data();
        data.setValue(this.dataValue);
        jaxbDestination.setData(data);
        return jaxbDestination;
    }

}
