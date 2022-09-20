package de.fhg.iais.roberta.syntax.actors.arduino.mbot;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "IR_SENDER", category = "ACTOR", blocklyNames = {"robCommunication_ir_sendBlock"})
public final class SendIRAction extends Action {

    @NepoValue(name = "sendData", type = BlocklyType.STRING)
    public final Expr message;

    public SendIRAction(BlocklyProperties properties, Expr message) {
        super(properties);
        this.message = message;
        setReadOnly();
    }
}
