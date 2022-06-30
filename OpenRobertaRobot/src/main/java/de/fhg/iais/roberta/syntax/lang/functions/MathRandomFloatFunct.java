package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

@NepoExpr(category = "FUNCTION", blocklyNames = {"math_random_float"}, name = "MATH_RANDOM_FLOAT_FUNCT", blocklyType = BlocklyType.NUMBER, precedence = 10)
public final class MathRandomFloatFunct<V> extends Function<V> {

    public MathRandomFloatFunct(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link MathRandomFloatFunct}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MathRandomFloatFunct}
     */
    public static <V> MathRandomFloatFunct<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathRandomFloatFunct<V>(properties, comment);
    }

}
