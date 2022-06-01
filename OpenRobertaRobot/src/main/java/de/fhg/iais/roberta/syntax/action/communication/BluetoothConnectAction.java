package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "BLUETOOTH_CONNECT_ACTION")
public class BluetoothConnectAction<V> extends Action<V> {
    @NepoValue(name = BlocklyConstants.ADDRESS, type = BlocklyType.STRING)
    public final Expr<V> address;

    public BluetoothConnectAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> address) {
        super(kind, properties, comment);
        Assert.isTrue(address.isReadOnly() && address != null);
        this.address = address;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorDriveStopAction}
     */
    public static <V> BluetoothConnectAction<V> make(Expr<V> address, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothConnectAction<V>(BlockTypeContainer.getByName("BLUETOOTH_CONNECT_ACTION"), properties, comment, address);
    }

    public Expr<V> getAddress() {
        return this.address;
    }

}
