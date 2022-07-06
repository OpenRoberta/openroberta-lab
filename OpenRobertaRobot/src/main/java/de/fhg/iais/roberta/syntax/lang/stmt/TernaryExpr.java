package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"logic_ternary"}, name = "IF_TERNARY")
public final class TernaryExpr extends Expr {
    @NepoValue(name = BlocklyConstants.IF, type = BlocklyType.BOOLEAN)
    public final Expr condition;
    @NepoValue(name = BlocklyConstants.THEN, type = BlocklyType.CAPTURED_TYPE)
    public final Expr thenPart;
    @NepoValue(name = BlocklyConstants.ELSE, type = BlocklyType.CAPTURED_TYPE)
    public final Expr elsePart;

    public TernaryExpr(
        BlocklyProperties properties,
        Expr condition,
        Expr thenPart,
        Expr elsePart) {
        super(properties);
        Assert.isTrue((condition != null) && (thenPart != null) && (elsePart != null));
        this.condition = condition;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
        setReadOnly();
    }

}
