package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoData;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "BLUETOOTH_RECEIVED_ACTION", category = "ACTOR", blocklyNames = {"robCommunication_receiveBlock"}, blocklyType = BlocklyType.STRING)
public final class BluetoothReceiveAction extends Action {

    @NepoMutation(fieldName = "datatype")
    public final Mutation mutation;

    @NepoField(name = "TYPE")
    public final String dataType;

    @NepoField(name = "PROTOCOL", value = "BLUETOOTH")
    public final String protocol;

    @NepoField(name = "CHANNEL", value = "-1")
    public final String channel;

    @NepoData
    public final String dataValue;

    @NepoValue(name = "CONNECTION", type = BlocklyType.CONNECTION)
    public final Expr connection;

    public BluetoothReceiveAction(
        BlocklyProperties properties, Mutation mutation, String dataType, String protocol, String channel, String dataValue, Expr bluetoothRecieveConnection) {
        super(properties);
        this.mutation = mutation;
        this.dataType = dataType;
        this.protocol = protocol;
        this.channel = channel;
        this.dataValue = dataValue;
        this.connection = bluetoothRecieveConnection;
        setReadOnly();
    }
}
