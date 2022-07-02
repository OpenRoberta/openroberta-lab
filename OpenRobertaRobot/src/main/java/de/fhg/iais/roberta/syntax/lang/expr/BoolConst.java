package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"logic_boolean"}, name = "BOOL_CONST", blocklyType = BlocklyType.BOOLEAN)
public final class BoolConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.BOOL)
    public final boolean value;

    public BoolConst(BlocklyBlockProperties properties, BlocklyComment comment, boolean value) {
        super(properties, comment);
        this.value = value;
        setReadOnly();
    }

}