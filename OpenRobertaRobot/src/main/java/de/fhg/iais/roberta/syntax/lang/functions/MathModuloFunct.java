package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(category = "FUNCTION", blocklyNames = {"math_modulo"}, name = "MATH_MODULO_FUNCTION", blocklyType = BlocklyType.NUMBER)
public final class MathModuloFunct extends Function {

    @NepoValue(name = "DIVIDEND", type = BlocklyType.NUMBER_INT)
    public final Expr dividend;

    @NepoValue(name = "DIVISOR", type = BlocklyType.NUMBER_INT)
    public final Expr divisor;

    public MathModuloFunct(BlocklyProperties properties, Expr dividend, Expr divisor) {
        super(properties);
        this.dividend = dividend;
        this.divisor = divisor;
        setReadOnly();
    }
}
