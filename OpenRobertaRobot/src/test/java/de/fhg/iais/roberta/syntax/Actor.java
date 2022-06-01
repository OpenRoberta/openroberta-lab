package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;

public class Actor extends Phrase<Void> {
    public Actor() {
        super(null, BlocklyBlockProperties.make("ACTOR", "actor"), null);
    }
}
