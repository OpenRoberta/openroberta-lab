package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "MATH_CONSTRAIN_FUNCT", category = "FUNCTION", blocklyNames = {"math_constrain"}, blocklyType = BlocklyType.NUMBER)
public final class MathConstrainFunct extends Function {

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr value;

    @NepoValue(name = "LOW", type = BlocklyType.NUMBER_INT)
    public final Expr lowerBound;

    @NepoValue(name = "HIGH", type = BlocklyType.NUMBER_INT)
    public final Expr upperBound;

    public MathConstrainFunct(BlocklyProperties properties, Expr value, Expr lowerBound, Expr upperBound) {
        super(properties);
        this.value = value;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        setReadOnly();
    }
}
