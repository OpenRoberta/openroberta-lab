package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "WALK_TO", category = "ACTOR", blocklyNames = {"naoActions_walkTo"})
public final class WalkTo<V> extends Action<V> {
    @NepoValue(name = "X", type = BlocklyType.NUMBER)
    public final Expr<V> walkToX;

    @NepoValue(name = "Y", type = BlocklyType.NUMBER)
    public final Expr<V> walkToY;

    @NepoValue(name = "Theta", type = BlocklyType.NUMBER)
    public final Expr<V> walkToTheta;

    public WalkTo(BlocklyProperties properties, Expr<V> walkToX, Expr<V> walkToY, Expr<V> walkToTheta) {
        super(properties);
        this.walkToX = walkToX;
        this.walkToY = walkToY;
        this.walkToTheta = walkToTheta;
        setReadOnly();
    }

}
