package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "SET_VOLUME", category = "ACTOR", blocklyNames = {"naoActions_setVolume"})
public final class SetVolume<V> extends Action<V> {
    @NepoValue(name = "VOLUME", type = BlocklyType.NUMBER_INT)
    public final Expr<V> volume;

    public SetVolume(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> volume) {
        super(properties, comment);
        this.volume = volume;
        setReadOnly();
    }

}
