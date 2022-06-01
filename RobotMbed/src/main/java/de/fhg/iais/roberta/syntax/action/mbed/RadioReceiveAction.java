package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;

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
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String type = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
        return RadioReceiveAction.make(BlocklyType.get(type), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDatatype(this.type.getBlocklyName());
        jaxbDestination.setMutation(mutation);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.type.getBlocklyName());
        return jaxbDestination;
    }
}
