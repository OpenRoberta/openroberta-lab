package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoBasic(name = "BOTH_MOTORS_STOP_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_motors_stop"})
public final class BothMotorsStopAction<V> extends Action<V> {

    public BothMotorsStopAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        return new BothMotorsStopAction<>(Jaxb2Ast.extractBlocklyProperties(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "SingleMotorStopAction []";
    }

}
