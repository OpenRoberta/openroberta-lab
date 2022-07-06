package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BOB3_SENDIR", category = "ACTOR", blocklyNames = {"bob3Communication_sendBlock"})
public final class SendIRAction extends Action {
    @NepoValue(name = "sendData", type = BlocklyType.NUMBER)
    public final Expr code;

    public SendIRAction(BlocklyProperties properties, Expr code) {
        super(properties);
        this.code = code;
        setReadOnly();
    }
}
