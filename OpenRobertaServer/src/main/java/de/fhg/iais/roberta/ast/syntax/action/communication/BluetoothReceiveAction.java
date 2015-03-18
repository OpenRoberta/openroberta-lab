package de.fhg.iais.roberta.ast.syntax.action.communication;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.*;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;

public class BluetoothReceiveAction<V> extends Action<V> {
    private final Expr<V> connection;
    
    private BluetoothReceiveAction(Expr<V> bluetoothRecieveConnection, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.BLUETOOTH_RECEIVED_ACTION, properties, comment);
        connection = bluetoothRecieveConnection;
        setReadOnly();
    }
    
    public static <V> BluetoothReceiveAction<V> make(Expr<V> bluetoothRecieveConnection, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothReceiveAction<V>(bluetoothRecieveConnection, properties, comment);
    }
    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitBluetoothReceiveAction(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.CONNECTION, getConnection());
        return jaxbDestination;
    }
    
    @Override
    public String toString(){
        return "ReceiveData[]";
    }
    
    public Expr<V> getConnection(){
        return connection;
    }
}
