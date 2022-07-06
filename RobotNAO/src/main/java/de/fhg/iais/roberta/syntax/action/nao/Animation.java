package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.action.nao.Move;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "ANIMATION", category = "ACTOR", blocklyNames = {"naoActions_animation"})
public final class Animation<V> extends Action<V> {
    @NepoField(name = "MOVE")
    public final Move move;

    public Animation(BlocklyProperties properties, Move move) {
        super(properties);
        Assert.notNull(move, "Missing Move in Mode block!");
        this.move = move;
        setReadOnly();
    }

}
