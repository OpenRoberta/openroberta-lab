package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoOp(containerType = "IF_TERNARY")
public class TernaryExpr<V> extends Expr<V> {
    @NepoValue(name = BlocklyConstants.IF, type = BlocklyType.BOOLEAN)
    public final Expr<V> condition;
    @NepoValue(name = BlocklyConstants.THEN, type = BlocklyType.CAPTURED_TYPE)
    public final Expr<V> thenPart;
    @NepoValue(name = BlocklyConstants.ELSE, type = BlocklyType.CAPTURED_TYPE)
    public final Expr<V> elsePart;

    public TernaryExpr(
        BlockType kind,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        Expr<V> condition,
        Expr<V> thenPart,
        Expr<V> elsePart) {
        super(kind, properties, comment);
        Assert.isTrue((condition != null) && (thenPart != null) && (elsePart != null));
        this.condition = condition;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
        setReadOnly();
    }

    public static <V> TernaryExpr<V> make(
        Expr<V> condition,
        Expr<V> thenPart,
        Expr<V> elsePart,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new TernaryExpr<V>(BlockTypeContainer.getByName("IF_TERNARY"), properties, comment, condition, thenPart, elsePart);
    }

    public Expr<V> getCondition() {
        return condition;
    }

    public Expr<V> getThenPart() {
        return thenPart;
    }

    public Expr<V> getElsePart() {
        return elsePart;
    }
}
