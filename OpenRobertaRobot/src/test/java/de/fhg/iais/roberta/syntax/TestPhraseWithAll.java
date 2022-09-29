package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoData;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "TEST_PHRASE_ALL", blocklyNames = {"test_phrase_all"}, category = "EXPR")
public final class TestPhraseWithAll extends Expr {

    @NepoMutation
    public final Mutation mutation;

    @NepoData
    public final String data;

    @NepoField(name = "TYPE")
    public final String type;

    @NepoHide
    public final Hide hide;

    public TestPhraseWithAll(
        BlocklyProperties properties,

        Mutation mutation,
        String data,
        String type,
        Hide hide) {
        super(properties);
        this.mutation = mutation;
        this.data = data;
        this.type = type;
        this.hide = hide;
    }
}
