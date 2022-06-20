package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_WRONG_MUTATION_TYPE", blocklyNames = {"test_phrase_wrong_mutation_type"}, category = "EXPR")
public class TestPhraseWrongMutationType<V> extends Phrase<V> {
    @NepoMutation
    public final String mutation;

    public TestPhraseWrongMutationType(BlocklyBlockProperties property, BlocklyComment comment, String mutation) {
        super(property, comment);
        this.mutation = mutation;
    }
}
