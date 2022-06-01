package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.transformer.NepoData;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase
public class TestPhraseWrongDataType<V> extends Phrase<V> {
    @NepoData
    public final Data data;

    public TestPhraseWrongDataType(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment, Data data) {
        super(kind, property, comment);
        this.data = data;
    }
}
