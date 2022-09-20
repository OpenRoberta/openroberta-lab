package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "WALK_ASYNC", category = "ACTOR", blocklyNames = {"naoActions_walk_async"})
public final class WalkAsync extends Action {

    @NepoValue(name = "XSpeed", type = BlocklyType.NUMBER_INT)
    public final Expr XSpeed;

    @NepoValue(name = "YSpeed", type = BlocklyType.NUMBER_INT)
    public final Expr YSpeed;

    @NepoValue(name = "ZSpeed", type = BlocklyType.NUMBER_INT)
    public final Expr ZSpeed;

    public WalkAsync(BlocklyProperties properties, Expr XSpeed, Expr YSpeed, Expr ZSpeed) {
        super(properties);
        this.XSpeed = XSpeed;
        this.YSpeed = YSpeed;
        this.ZSpeed = ZSpeed;
        setReadOnly();
    }
}
