package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoExpr(precedence = 10, assoc = Assoc.LEFT, blocklyType = BlocklyType.ARRAY, containerType = "TEST_OPERATION", category = "EXPR", blocklyNames = {"test_operation"})
public class TestOperation<V> extends Expr<V> {
    public TestOperation(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
    }
}
