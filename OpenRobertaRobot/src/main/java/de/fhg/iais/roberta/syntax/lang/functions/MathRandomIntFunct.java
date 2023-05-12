package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "MATH_RANDOM_INT_FUNCT", category = "FUNCTION", blocklyNames = {"math_random_int"}, blocklyType = BlocklyType.NUMBER_INT)
public final class MathRandomIntFunct extends Function {

    @NepoValue(name = "FROM", type = BlocklyType.NUMBER_INT)
    public final Expr from;

    @NepoValue(name = "TO", type = BlocklyType.NUMBER_INT)
    public final Expr to;

    public MathRandomIntFunct(BlocklyProperties properties, Expr from, Expr to) {
        super(properties);
        this.from = from;
        this.to = to;
        setReadOnly();
    }
}
