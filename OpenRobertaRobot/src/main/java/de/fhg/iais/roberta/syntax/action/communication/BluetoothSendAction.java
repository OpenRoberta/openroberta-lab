package de.fhg.iais.roberta.syntax.action.communication;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
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

@NepoBasic(name = "BLUETOOTH_SEND_ACTION", category = "ACTOR", blocklyNames = {"robCommunication_sendBlock"})
public final class BluetoothSendAction<V> extends Action<V> {
    public final Expr<V> connection;
    public final Expr<V> msg;
    public String channel;
    public String dataType;
    public final String dataValue;


    public BluetoothSendAction(
        String dataValue,
        Expr<V> connection,
        Expr<V> msg,
        String channel,
        String dataType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) //
    {
        super(properties, comment);
        this.connection = connection;
        this.msg = msg;
        this.channel = channel;
        this.dataType = dataType;
        this.dataValue = dataValue;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "BluetoothSendAction [" + this.connection.toString() + ", " + this.msg.toString() + ", " + this.channel + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        Phrase<V> bluetoothSendMessage = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.STRING));
        Phrase<V> bluetoothSendConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, BlocklyType.NULL));
        Data data = block.getData();
        String dataValue = data.getValue();
        if ( fields.size() == 3 ) {
            String bluetoothSendChannel = Jaxb2Ast.extractField(fields, BlocklyConstants.CHANNEL);
            String bluetoothRecieveDataType = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
            return new BluetoothSendAction<>(dataValue, Jaxb2Ast.convertPhraseToExpr(bluetoothSendConnection), Jaxb2Ast.convertPhraseToExpr(bluetoothSendMessage), bluetoothSendChannel, bluetoothRecieveDataType, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        } else {
            String bluetoothSendChannel = "-1";
            String bluetoothRecieveDataType = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
            return new BluetoothSendAction<>(dataValue, Jaxb2Ast.convertPhraseToExpr(bluetoothSendConnection), Jaxb2Ast.convertPhraseToExpr(bluetoothSendMessage), bluetoothSendChannel, bluetoothRecieveDataType, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Mutation mutation = new Mutation();
        mutation.setDatatype(this.dataType);
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.dataType);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PROTOCOL, "BLUETOOTH");
        if ( !this.channel.equals("-1") ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.CHANNEL, this.channel);
        }
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.MESSAGE, this.msg);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.CONNECTION, this.connection);
        Data data = new Data();
        data.setValue(this.dataValue);
        jaxbDestination.setData(data);
        return jaxbDestination;
    }

}
