package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(precedence = 10, assoc = Assoc.LEFT, blocklyType = BlocklyType.ARRAY, name = "TEST_OPERATION", category = "EXPR", blocklyNames = {"test_operation"})
public final class TestOperation extends Expr {
    public TestOperation(BlocklyProperties properties) {
        super(properties);
    }
}
