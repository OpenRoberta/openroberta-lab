package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(category = "FUNCTION", blocklyNames = {"math_random_float"}, name = "MATH_RANDOM_FLOAT_FUNCT", blocklyType = BlocklyType.NUMBER, precedence = 10)
public final class MathRandomFloatFunct extends Function {

    public MathRandomFloatFunct(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

}
