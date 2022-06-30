package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "RASTA_DURATION", category = "ACTOR", blocklyNames = {"naoActions_rasta"})
public final class RastaDuration<V> extends Action<V> {
    @NepoValue(name = "DURATION", type = BlocklyType.NUMBER_INT)
    public final Expr<V> duration;

    public RastaDuration(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> duration) {
        super(properties, comment);
        this.duration = duration;
        setReadOnly();
    }

    public Expr<V> getDuration() {
        return this.duration;
    }

}
