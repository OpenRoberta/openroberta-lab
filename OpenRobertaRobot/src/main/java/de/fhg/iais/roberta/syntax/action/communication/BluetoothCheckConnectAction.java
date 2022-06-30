package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robCommunication_checkConnection"}, name = "BLUETOOTH_CHECK_CONNECT_ACTION")
public final class BluetoothCheckConnectAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.CONNECTION, type = BlocklyType.STRING)
    public final Expr<V> connection;

    public BluetoothCheckConnectAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> connection) {
        super(properties, comment);
        Assert.isTrue(connection.isReadOnly() && connection != null);
        this.connection = connection;
        setReadOnly();
    }
    
    public static <V> BluetoothCheckConnectAction<V> make(Expr<V> connection, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothCheckConnectAction<V>(properties, comment, connection);
    }

    public Expr<V> getConnection() {
        return this.connection;
    }

}
