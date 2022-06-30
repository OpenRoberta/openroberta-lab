package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "PLAY_FILE", category = "ACTOR", blocklyNames = {"naoActions_playFile"})
public final class PlayFile<V> extends Action<V> {
    @NepoField(name = "OUT")
    public final Expr<V> msg;

    public PlayFile(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> msg) {
        super(properties, comment);
        Assert.isTrue(msg != null);
        this.msg = msg;
        setReadOnly();
    }

    public Expr<V> getMsg() {
        return this.msg;
    }
}
