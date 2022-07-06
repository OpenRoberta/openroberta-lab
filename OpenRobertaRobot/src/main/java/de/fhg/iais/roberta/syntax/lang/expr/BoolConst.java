package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"logic_boolean"}, name = "BOOL_CONST", blocklyType = BlocklyType.BOOLEAN)
public final class BoolConst extends Expr {
    @NepoField(name = BlocklyConstants.BOOL)
    public final boolean value;

    public BoolConst(BlocklyProperties properties, boolean value) {
        super(properties);
        this.value = value;
        setReadOnly();
    }

}