package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "ANIMATION", category = "ACTOR", blocklyNames = {"naoActions_animation"})
public final class Animation extends Action {

    @NepoField(name = "MOVE")
    public final String move;

    public Animation(BlocklyProperties properties, String move) {
        super(properties);
        Assert.notNull(move, "Missing Move in Mode block!");
        this.move = move;
        setReadOnly();
    }
}
