package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RASTA_DURATION", category = "ACTOR", blocklyNames = {"naoActions_rasta"})
public final class RastaDuration extends Action {
    @NepoValue(name = "DURATION", type = BlocklyType.NUMBER_INT)
    public final Expr duration;

    public RastaDuration(BlocklyProperties properties, Expr duration) {
        super(properties);
        this.duration = duration;
        setReadOnly();
    }

}
