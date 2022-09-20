package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoExpr(name = "LEARN_FACE", category = "ACTOR", blocklyNames = {"naoActions_learnFace"}, blocklyType = BlocklyType.BOOLEAN)
public final class LearnFace extends Action {
    
    @NepoValue(name = "NAME", type = BlocklyType.STRING)
    public final Expr faceName;

    public LearnFace(BlocklyProperties properties, Expr faceName) {
        super(properties);
        Assert.isTrue(faceName != null);
        this.faceName = faceName;
        setReadOnly();
    }
}
