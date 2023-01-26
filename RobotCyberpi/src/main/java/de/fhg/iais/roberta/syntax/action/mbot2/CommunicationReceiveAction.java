package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "COMMUNICATION_RECEIVE_ACTION", category = "ACTOR", blocklyNames = {"communication_receive_message"}, blocklyType = BlocklyType.STRING)

public final class CommunicationReceiveAction extends Action {

    @NepoValue(name = "CHANNEL", type = BlocklyType.STRING)
    public final Expr channel;

    public CommunicationReceiveAction(BlocklyProperties properties, Expr channel) {
        super(properties);
        this.channel = channel;
        setReadOnly();
    }
}
