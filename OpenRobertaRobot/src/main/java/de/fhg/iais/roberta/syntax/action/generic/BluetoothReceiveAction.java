package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;

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
        super(BlockTypeContainer.getByName("BLUETOOTH_RECEIVED_ACTION"),properties, comment);
        this.connection = bluetoothRecieveConnection;
        this.channel = channel;
        this.dataType = dataType;
        setReadOnly();
    }

    private BluetoothReceiveAction(Expr<V> bluetoothRecieveConnection, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BLUETOOTH_RECEIVED_ACTION"),properties, comment);
        this.connection = bluetoothRecieveConnection;
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

    public static <V> BluetoothReceiveAction<V> make(Expr<V> bluetoothRecieveConnection, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothReceiveAction<V>(bluetoothRecieveConnection, properties, comment);
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBluetoothReceiveAction(this);
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
        List<Field> fields = helper.extractFields(block, (short) 3);
        Phrase<V> bluetoothRecieveConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, Null.class));
        if ( fields.size() == 3 ) {
            String bluetoothRecieveChannel = helper.extractField(fields, BlocklyConstants.CHANNEL);
            String bluetoothRecieveDataType = helper.extractField(fields, BlocklyConstants.TYPE);
            return BluetoothReceiveAction.make(
                helper.convertPhraseToExpr(bluetoothRecieveConnection),
                bluetoothRecieveChannel,
                bluetoothRecieveDataType,
                helper.extractBlockProperties(block),
                helper.extractComment(block));
        } else {
            return BluetoothReceiveAction
                .make(helper.convertPhraseToExpr(bluetoothRecieveConnection), helper.extractBlockProperties(block), helper.extractComment(block));
        }
    }

    //TODO: add tests for NXT blocks
    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "BluetoothReceiveAction [connection=" + this.connection + ", " + this.channel + ", " + this.dataType + "]";
    }

}
