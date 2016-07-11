package de.fhg.iais.roberta.syntax.action.ev3;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;

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
import de.fhg.iais.roberta.visitor.AstVisitor;

public class BluetoothReceiveAction<V> extends Action<V> {
    private final Expr<V> connection;

    private BluetoothReceiveAction(Expr<V> bluetoothRecieveConnection, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockType.BLUETOOTH_RECEIVED_ACTION, properties, comment);
        this.connection = bluetoothRecieveConnection;
        setReadOnly();
    }

    public static <V> BluetoothReceiveAction<V> make(Expr<V> bluetoothRecieveConnection, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothReceiveAction<V>(bluetoothRecieveConnection, properties, comment);
    }

    public Expr<V> getConnection() {
        return this.connection;
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBluetoothReceiveAction(this);
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
        Phrase<V> bluetoothRecieveConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, Null.class));
        return BluetoothReceiveAction
            .make(helper.convertPhraseToExpr(bluetoothRecieveConnection), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "BluetoothReceiveAction [connection=" + this.connection + "]";
    }

}
