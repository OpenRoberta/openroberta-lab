package de.fhg.iais.roberta.syntax.actors.arduino.mbot;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "IR_RECEIVER", category = "ACTOR", blocklyNames = {"robCommunication_ir_receiveBlock"})
public final class ReceiveIRAction<V> extends Action<V> {

    public ReceiveIRAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

    @Override
    public String toString() {
        return "ReceiveIRAction";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        return new ReceiveIRAction<>(Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;

    }
}
