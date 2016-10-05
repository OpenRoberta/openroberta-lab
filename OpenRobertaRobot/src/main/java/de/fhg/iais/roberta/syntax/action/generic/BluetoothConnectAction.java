package de.fhg.iais.roberta.syntax.action.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

public class BluetoothConnectAction<V> extends Action<V> {
    private final Expr<V> _address;

    private BluetoothConnectAction(Expr<V> address, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BLUETOOTH_CONNECT_ACTION"),properties, comment);
        Assert.isTrue(address.isReadOnly() && address != null);
        this._address = address;
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
        return new BluetoothConnectAction<V>(address, properties, comment);
    }

    public Expr<V> get_address() {
        return this._address;
    }

    @Override
    public String toString() {
        return "BluetoothConnectAction [" + get_address().toString() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBluetoothConnectAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> bluetoothConnectAddress = helper.extractValue(values, new ExprParam(BlocklyConstants.ADDRESS, String.class));
        return BluetoothConnectAction
            .make(helper.convertPhraseToExpr(bluetoothConnectAddress), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.ADDRESS, get_address());

        return jaxbDestination;
    }

}
