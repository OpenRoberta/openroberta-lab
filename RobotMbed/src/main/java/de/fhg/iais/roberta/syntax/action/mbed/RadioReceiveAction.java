package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "RADIO_RECEIVE_ACTION", category = "ACTOR", blocklyNames = {"mbedCommunication_receiveBlock"})
public final class RadioReceiveAction<V> extends Action<V> {
    public final BlocklyType type;

    public RadioReceiveAction(BlocklyProperties properties, BlocklyType type) {
        super(properties);
        this.type = type;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "BluetoothReceiveAction [" + this.type.toString() + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String type = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
        return new RadioReceiveAction<>(Jaxb2Ast.extractBlocklyProperties(block), BlocklyType.get(type));

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
