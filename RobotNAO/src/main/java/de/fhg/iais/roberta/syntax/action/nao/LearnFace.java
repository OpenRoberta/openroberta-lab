package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "LEARN_FACE", category = "ACTOR", blocklyNames = {"naoActions_learnFace"})
public final class LearnFace<V> extends Action<V> {
    @NepoValue(name = "NAME", type = BlocklyType.STRING)
    public final Expr<V> faceName;

    public LearnFace(BlocklyProperties properties, Expr<V> faceName) {
        super(properties);
        Assert.isTrue(faceName != null);
        this.faceName = faceName;
        setReadOnly();
    }

}
