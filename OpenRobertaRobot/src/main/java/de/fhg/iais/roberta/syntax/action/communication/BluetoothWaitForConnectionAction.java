package de.fhg.iais.roberta.syntax.action.communication;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IBluetoothVisitor;

public class BluetoothWaitForConnectionAction<V> extends Action<V> {

    private BluetoothWaitForConnectionAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BLUETOOTH_WAIT_FOR_CONNECTION_ACTION"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorDriveStopAction}
     */
    public static <V> BluetoothWaitForConnectionAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BluetoothWaitForConnectionAction<V>(properties, comment);
    }

    @Override
    public String toString() {
        return "BluetoothWaitForConnectionAction []";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IBluetoothVisitor<V>) visitor).visitBluetoothWaitForConnectionAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return BluetoothWaitForConnectionAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }
}
