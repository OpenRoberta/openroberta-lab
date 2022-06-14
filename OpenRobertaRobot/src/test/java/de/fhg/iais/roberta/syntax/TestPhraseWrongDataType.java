package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoData;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_WRONG_DATA_TYPE", blocklyNames = {"test_phrase_wrong_datatype"}, category = "EXPR")
public class TestPhraseWrongDataType<V> extends Phrase<V> {
    @NepoData
    public final Data data;

    public TestPhraseWrongDataType(BlocklyBlockProperties property, BlocklyComment comment, Data data) {
        super(property, comment);
        this.data = data;
    }
}
