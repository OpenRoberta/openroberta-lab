package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(category = "EXPR", blocklyNames = {"logic_null"}, name = "NULL_CONST", blocklyType = BlocklyType.NULL)
public final class NullConst extends Expr {
    public NullConst(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

}
