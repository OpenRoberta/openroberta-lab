package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(containerType = "BLUETOOTH_CHECK_CONNECT_ACTION")
public class BluetoothCheckConnectAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.CONNECTION, type = BlocklyType.STRING)
    public final Expr<V> connection;

    public BluetoothCheckConnectAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> connection) {
        super(kind, properties, comment);
        Assert.isTrue(connection.isReadOnly() && connection != null);
        this.connection = connection;
        setReadOnly();
    }

    /**
     * Creates instance of {@link BluetoothCheckConnectAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BluetoothCheckConnectAction}
     */
    public static <V> BluetoothCheckConnectAction<V> make(Expr<V> connection, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothCheckConnectAction<V>(BlockTypeContainer.getByName("BLUETOOTH_CHECK_CONNECT_ACTION"), properties, comment, connection);
    }

    public Expr<V> getConnection() {
        return this.connection;
    }

}
