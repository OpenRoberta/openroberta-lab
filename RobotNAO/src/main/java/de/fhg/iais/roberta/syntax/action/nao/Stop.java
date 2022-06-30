package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "STOP", category = "ACTOR", blocklyNames = {"naoActions_stop"})
public final class Stop<V> extends Action<V> {

    public Stop(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }

    public static <V> Stop<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Stop<V>(properties, comment);
    }
}
