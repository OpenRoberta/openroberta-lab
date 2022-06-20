package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;

@NepoBasic(containerType = "ACTOR", category = "ACTOR", blocklyNames = {"actor"})
public final class Actor extends Phrase<Void> {
    public Actor() {
        super(BlocklyBlockProperties.make("ACTOR", "1"), null);
    }
}
