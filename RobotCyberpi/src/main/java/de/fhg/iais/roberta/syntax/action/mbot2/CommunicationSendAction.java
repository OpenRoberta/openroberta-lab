package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "COMMUNICATION_SEND_ACTION", category = "ACTOR", blocklyNames = {"communication_send_message"})

public final class CommunicationSendAction extends Action {

    @NepoValue(name = "CHANNEL", type = BlocklyType.STRING)
    public final Expr channel;

    @NepoValue(name = "MESSAGE", type = BlocklyType.STRING)
    public final Expr message;

    public CommunicationSendAction(BlocklyProperties properties, Expr channel, Expr message) {
        super(properties);
        this.message = message;
        this.channel = channel;
        setReadOnly();
    }
}
