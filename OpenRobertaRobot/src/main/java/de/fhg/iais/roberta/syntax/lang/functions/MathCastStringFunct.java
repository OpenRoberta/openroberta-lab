package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoExpr(name = "MATH_CAST_STRING_FUNCT", category = "FUNCTION", blocklyNames = {"math_cast_toString"}, blocklyType = BlocklyType.STRING)
public final class MathCastStringFunct extends Function {

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr value;

    public MathCastStringFunct(BlocklyProperties properties, Expr value) {
        super(properties);
        Assert.isTrue(value != null);
        this.value = value;
        setReadOnly();
    }
}
