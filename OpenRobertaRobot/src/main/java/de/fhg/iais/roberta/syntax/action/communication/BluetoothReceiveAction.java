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
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
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
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        Phrase<V> bluetoothRecieveConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, BlocklyType.NULL));
        if ( fields.size() == 3 ) {
            String bluetoothRecieveChannel = Jaxb2Ast.extractField(fields, BlocklyConstants.CHANNEL);
            String bluetoothRecieveDataType = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
            return BluetoothReceiveAction
                .make(
                    helper.convertPhraseToExpr(bluetoothRecieveConnection),
                    bluetoothRecieveChannel,
                    bluetoothRecieveDataType,
                    Jaxb2Ast.extractBlockProperties(block),
                    Jaxb2Ast.extractComment(block));
        } else {
            String bluetoothReceiveChannel = "-1";
            String bluetoothRecieveDataType = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
            return BluetoothReceiveAction
                .make(
                    helper.convertPhraseToExpr(bluetoothRecieveConnection),
                    bluetoothReceiveChannel,
                    bluetoothRecieveDataType,
                    Jaxb2Ast.extractBlockProperties(block),
                    Jaxb2Ast.extractComment(block));
        }
    }

    //TODO: add tests for NXT blocks
    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();

        Mutation mutation = new Mutation();
        mutation.setDatatype(this.dataType);
        jaxbDestination.setMutation(mutation);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.dataType);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PROTOCOL, "BLUETOOTH");
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.CHANNEL, getChannel());

        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "BluetoothReceiveAction [connection=" + this.connection + ", " + this.channel + ", " + this.dataType + "]";
    }

}
