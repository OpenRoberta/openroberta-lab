package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.action.nao.Move;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "ANIMATION", category = "ACTOR", blocklyNames = {"naoActions_animation"})
public final class Animation<V> extends Action<V> {
    @NepoField(name = "MOVE")
    public final Move move;

    public Animation(BlocklyBlockProperties properties, BlocklyComment comment, Move move) {
        super(properties, comment);
        Assert.notNull(move, "Missing Move in Mode block!");
        this.move = move;
        setReadOnly();
    }

    public static <V> Animation<V> make(Move move, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Animation<V>(properties, comment, move);
    }

    public Move getMove() {
        return this.move;
    }
}
