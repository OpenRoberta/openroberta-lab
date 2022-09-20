package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BOB3_REMEMBER", category = "ACTOR", blocklyNames = {"bob3Actions_remember"})
public final class RememberAction extends Action {

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER)
    public final Expr code;

    public RememberAction(BlocklyProperties properties, Expr code) {
        super(properties);
        this.code = code;
        setReadOnly();
    }
}
