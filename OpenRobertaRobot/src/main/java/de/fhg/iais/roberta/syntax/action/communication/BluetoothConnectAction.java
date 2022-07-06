package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robCommunication_startConnection"}, name = "BLUETOOTH_CONNECT_ACTION")
public final class BluetoothConnectAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.ADDRESS, type = BlocklyType.STRING)
    public final Expr<V> address;

    public BluetoothConnectAction(BlocklyProperties properties, Expr<V> address) {
        super(properties);
        Assert.isTrue(address.isReadOnly() && address != null);
        this.address = address;
        setReadOnly();
    }

}
