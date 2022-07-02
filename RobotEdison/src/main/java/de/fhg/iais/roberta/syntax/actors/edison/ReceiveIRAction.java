package de.fhg.iais.roberta.syntax.actors.edison;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(category = "ACTOR", blocklyNames = {"edisonCommunication_ir_receiveBlock"}, name = "IR_RECV")
public final class ReceiveIRAction<V> extends Action<V> {

    public ReceiveIRAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }

}