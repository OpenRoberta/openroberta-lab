package de.fhg.iais.roberta.ast.syntax.action.communication;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

public class BluetoothReceiveAction<V> extends Action<V> {
    private final Expr<V> connection;

    private BluetoothReceiveAction(Expr<V> bluetoothRecieveConnection, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.BLUETOOTH_RECEIVED_ACTION, properties, comment);
        Assert.isTrue(bluetoothRecieveConnection.isReadOnly() && bluetoothRecieveConnection != null);
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
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> bluetoothRecieveConnection = helper.extractValue(values, new ExprParam(BlocklyConstants.CONNECTION, null));
        return BluetoothReceiveAction.make(
            helper.convertPhraseToExpr(bluetoothRecieveConnection),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "ReceiveData[]";
    }

}
