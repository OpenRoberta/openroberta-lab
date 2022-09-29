package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_WRONG_MUTATION_TYPE", blocklyNames = {"test_phrase_wrong_mutation_type"}, category = "EXPR")
public final class TestPhraseWrongMutationType extends Phrase {
    @NepoMutation
    public final String mutation;

    public TestPhraseWrongMutationType(BlocklyProperties property, String mutation) {
        super(property);
        this.mutation = mutation;
    }
}
