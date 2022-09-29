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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "BLUETOOTH_RECEIVED_ACTION", category = "ACTOR", blocklyNames = {"robCommunication_receiveBlock"})
public final class BluetoothReceiveAction extends Action {
    public final Expr connection;
    public final String dataValue;
    public final String channel;
    public final String dataType;

    public BluetoothReceiveAction(
        String dataValue,
        Expr bluetoothRecieveConnection,
        String channel,
        String dataType,
        BlocklyProperties properties) {
        super(properties);
        this.connection = bluetoothRecieveConnection;
        this.channel = channel;
        this.dataType = dataType;
        this.dataValue = dataValue;
        setReadOnly();
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        Phrase bluetoothRecieveConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, BlocklyType.NULL));
        Data data = block.getData();
        String dataValue = data.getValue();
        if ( fields.size() == 3 ) {
            String bluetoothRecieveChannel = Jaxb2Ast.extractField(fields, BlocklyConstants.CHANNEL);
            String bluetoothRecieveDataType = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
            return new BluetoothReceiveAction(dataValue, Jaxb2Ast.convertPhraseToExpr(bluetoothRecieveConnection), bluetoothRecieveChannel, bluetoothRecieveDataType, Jaxb2Ast.extractBlocklyProperties(block));
        } else {
            String bluetoothReceiveChannel = "-1";
            String bluetoothRecieveDataType = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
            return new BluetoothReceiveAction(dataValue, Jaxb2Ast.convertPhraseToExpr(bluetoothRecieveConnection), bluetoothReceiveChannel, bluetoothRecieveDataType, Jaxb2Ast.extractBlocklyProperties(block));
        }
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Mutation mutation = new Mutation();
        mutation.setDatatype(this.dataType);
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.dataType);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PROTOCOL, "BLUETOOTH");
        if ( !"-1".equals(this.channel) ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.CHANNEL, this.channel);
        }
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.CONNECTION, this.connection);
        Data data = new Data();
        data.setValue(this.dataValue);
        jaxbDestination.setData(data);
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "BluetoothReceiveAction [connection=" + this.connection + ", " + this.channel + ", " + this.dataType + "]";
    }

}
