package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "ACTOR", blocklyNames = {"robCommunication_startConnection"}, name = "BLUETOOTH_CONNECT_ACTION", blocklyType = BlocklyType.CONNECTION)
public final class BluetoothConnectAction extends Action {

    @NepoValue(name = BlocklyConstants.ADDRESS, type = BlocklyType.STRING)
    public final Expr address;

    public BluetoothConnectAction(BlocklyProperties properties, Expr address) {
        super(properties);
        Assert.isTrue(address.isReadOnly() && address != null);
        this.address = address;
        setReadOnly();
    }
}
