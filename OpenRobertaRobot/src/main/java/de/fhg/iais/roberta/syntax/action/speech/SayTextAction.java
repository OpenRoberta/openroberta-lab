package de.fhg.iais.roberta.syntax.action.speech;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"naoActions_sayText", "robActions_sayText"}, name = "SAY_TEXT")
public final class SayTextAction extends Action {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.STRING)
    public final Expr msg;

    public SayTextAction(BlocklyProperties properties, Expr msg) {
        super(properties);
        Assert.isTrue(msg != null);
        this.msg = msg;
        setReadOnly();
    }

}
