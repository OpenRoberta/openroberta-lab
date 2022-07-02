package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "LEARN_FACE", category = "ACTOR", blocklyNames = {"naoActions_learnFace"})
public final class LearnFace<V> extends Action<V> {
    @NepoValue(name = "NAME", type = BlocklyType.STRING)
    public final Expr<V> faceName;

    public LearnFace(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> faceName) {
        super(properties, comment);
        Assert.isTrue(faceName != null);
        this.faceName = faceName;
        setReadOnly();
    }

}
