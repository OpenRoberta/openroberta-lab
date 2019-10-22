package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbedVisitor;

public class RadioReceiveAction<V> extends Action<V> {
    private final BlocklyType type;

    private RadioReceiveAction(BlocklyType type, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RADIO_RECEIVE_ACTION"), properties, comment);
        this.type = type;
        setReadOnly();
    }

    public static <V> RadioReceiveAction<V> make(BlocklyType type, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RadioReceiveAction<>(type, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IMbedVisitor<V>) visitor).visitRadioReceiveAction(this);

    }

    @Override
    public String toString() {
        return "BluetoothReceiveAction [" + getType().toString() + "]";
    }

    public BlocklyType getType() {
        return this.type;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String type = helper.extractField(fields, BlocklyConstants.TYPE);
        return RadioReceiveAction.make(BlocklyType.get(type), helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDatatype(this.type.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TYPE, this.type.getBlocklyName());
        return jaxbDestination;
    }
}
