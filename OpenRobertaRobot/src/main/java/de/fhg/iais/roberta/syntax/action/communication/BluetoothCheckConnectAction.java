package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robCommunication_checkConnection"}, name = "BLUETOOTH_CHECK_CONNECT_ACTION")
public final class BluetoothCheckConnectAction extends Action {
    @NepoValue(name = BlocklyConstants.CONNECTION, type = BlocklyType.STRING)
    public final Expr connection;

    public BluetoothCheckConnectAction(BlocklyProperties properties, Expr connection) {
        super(properties);
        Assert.isTrue(connection.isReadOnly() && connection != null);
        this.connection = connection;
        setReadOnly();
    }

}
