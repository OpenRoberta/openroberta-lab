package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"logic_ternary"}, containerType = "IF_TERNARY")
public class TernaryExpr<V> extends Expr<V> {
    @NepoValue(name = BlocklyConstants.IF, type = BlocklyType.BOOLEAN)
    public final Expr<V> condition;
    @NepoValue(name = BlocklyConstants.THEN, type = BlocklyType.CAPTURED_TYPE)
    public final Expr<V> thenPart;
    @NepoValue(name = BlocklyConstants.ELSE, type = BlocklyType.CAPTURED_TYPE)
    public final Expr<V> elsePart;

    public TernaryExpr(
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        Expr<V> condition,
        Expr<V> thenPart,
        Expr<V> elsePart) {
        super(properties, comment);
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
        return new TernaryExpr<V>(properties, comment, condition, thenPart, elsePart);
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
