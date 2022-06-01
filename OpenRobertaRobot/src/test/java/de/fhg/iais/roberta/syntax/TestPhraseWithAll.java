package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoData;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoMutation;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(containerType = "TEST_PHRASE_ALL")
public class TestPhraseWithAll<V> extends Expr<V> {

    @NepoMutation
    public final Mutation mutation;

    @NepoData
    public final String data;

    @NepoField(name = "TYPE")
    public final String type;

    @NepoHide
    public final Hide hide;

    public TestPhraseWithAll(
        BlockType kind,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        Mutation mutation,
        String data,
        String type,
        Hide hide) {
        super(kind, properties, comment);
        this.mutation = mutation;
        this.data = data;
        this.type = type;
        this.hide = hide;
    }
}
