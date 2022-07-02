package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "BOB3_SENDIR", category = "ACTOR", blocklyNames = {"bob3Communication_sendBlock"})
public final class SendIRAction<V> extends Action<V> {
    @NepoValue(name = "sendData", type = BlocklyType.NUMBER)
    public final Expr<V> code;

    public SendIRAction(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> code) {
        super(properties, comment);
        this.code = code;
        setReadOnly();
    }
}
