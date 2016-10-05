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

public class BluetoothCheckConnectAction<V> extends Action<V> {
    private final Expr<V> _connection;

    private BluetoothCheckConnectAction(Expr<V> _connection, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BLUETOOTH_CHECK_CONNECT_ACTION"),properties, comment);
        Assert.isTrue(_connection.isReadOnly() && _connection != null);
        this._connection = _connection;
        setReadOnly();
    }

    /**
     * Creates instance of {@link BluetoothCheckConnectAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BluetoothCheckConnectAction}
     */
    public static <V> BluetoothCheckConnectAction<V> make(Expr<V> _connection, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothCheckConnectAction<V>(_connection, properties, comment);
    }

    public Expr<V> getConnection() {
        return this._connection;
    }

    @Override
    public String toString() {
        return "BluetoothConnectAction [" + getConnection().toString() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBluetoothCheckConnectAction(this);
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
        Phrase<V> bluetoothConnectAddress = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, String.class));
        return BluetoothCheckConnectAction
            .make(helper.convertPhraseToExpr(bluetoothConnectAddress), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());

        return jaxbDestination;
    }

}
