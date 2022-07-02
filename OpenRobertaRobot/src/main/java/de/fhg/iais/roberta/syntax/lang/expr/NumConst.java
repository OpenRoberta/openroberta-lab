package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"math_integer", "math_number", "rob_math_u999"}, name = "NUM_CONST", blocklyType = BlocklyType.NUMBER)
public final class NumConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.NUM)
    public final String value;

    public NumConst(BlocklyBlockProperties properties, BlocklyComment comment, String value) {
        super(properties == null ? BlocklyBlockProperties.make("NUM_CONST", "1") : properties, comment);
        Assert.isTrue(!value.equals(""));
        this.value = value;
        setReadOnly();
    }

}
