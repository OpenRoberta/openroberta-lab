package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "PLAY_FILE", category = "ACTOR", blocklyNames = {"naoActions_playFile"})
public final class PlayFile<V> extends Action<V> {
    @NepoValue(name = "OUT")
    public final Expr<V> msg;

    public PlayFile(BlocklyProperties properties, Expr<V> msg) {
        super(properties);
        Assert.isTrue(msg != null);
        this.msg = msg;
        setReadOnly();
    }

}
