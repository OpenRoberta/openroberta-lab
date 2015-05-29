package de.fhg.iais.roberta.syntax.action.ev3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockType;
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

public class BluetoothSendAction<V> extends Action<V> {

    private final Expr<V> _connection;
    private final Expr<V> _msg;

    private BluetoothSendAction(Expr<V> connection, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockType.BLUETOOTH_SEND_ACTION, properties, comment);
        Assert.isTrue(connection.isReadOnly() && connection != null && msg.isReadOnly() && msg != null);
        this._connection = connection;
        this._msg = msg;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorDriveStopAction}
     */
    public static <V> BluetoothSendAction<V> make(Expr<V> connection, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothSendAction<V>(connection, msg, properties, comment);
    }

    public Expr<V> get_connection() {
        return this._connection;
    }

    public Expr<V> get_msg() {
        return this._msg;
    }

    @Override
    public String toString() {
        return "BluetoothSendAction [" + get_connection().toString() + ", " + get_msg().toString() + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBluetoothSendAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 2);
        Phrase<V> bluetoothSendMessage = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, String.class));
        Phrase<V> bluetoothSendConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, null));
        return BluetoothSendAction.make(
            helper.convertPhraseToExpr(bluetoothSendConnection),
            helper.convertPhraseToExpr(bluetoothSendMessage),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.MESSAGE, get_msg());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, get_connection());

        return jaxbDestination;
    }

}
