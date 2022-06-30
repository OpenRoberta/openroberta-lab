package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoExpr(category = "EXPR", blocklyNames = {"logic_null"}, name = "NULL_CONST", blocklyType = BlocklyType.NULL)
public final class NullConst<V> extends Expr<V> {
    public NullConst(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }

    public static <V> NullConst<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NullConst<V>(properties, comment);
    }
}
