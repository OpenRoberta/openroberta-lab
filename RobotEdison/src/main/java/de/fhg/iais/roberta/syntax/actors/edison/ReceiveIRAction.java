package de.fhg.iais.roberta.syntax.actors.edison;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"edisonCommunication_ir_receiveBlock"}, name = "IR_RECV")
public final class ReceiveIRAction<V> extends Action<V> {

    public ReceiveIRAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

}