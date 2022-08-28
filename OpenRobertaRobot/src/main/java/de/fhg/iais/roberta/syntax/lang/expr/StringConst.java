package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"text"}, name = "STRING_CONST", blocklyType = BlocklyType.STRING)
public final class StringConst extends Expr {
    @NepoField(name = BlocklyConstants.TEXT)
    public final String value;

    public StringConst(BlocklyProperties properties, String value) {
        super(properties);
        this.value = value;
        setReadOnly();
    }

}
