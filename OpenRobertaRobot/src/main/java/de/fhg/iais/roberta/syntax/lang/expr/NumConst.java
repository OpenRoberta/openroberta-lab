package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"math_integer", "math_number", "rob_math_u999"}, name = "NUM_CONST", blocklyType = BlocklyType.NUMBER)
public final class NumConst extends Expr {
    @NepoField(name = BlocklyConstants.NUM)
    public final String value;

    public NumConst(BlocklyProperties properties, String value) {
        super(properties == null ? BlocklyProperties.make("NUM_CONST", "1") : properties);
        Assert.isTrue(!value.equals(""));
        this.value = Util.sanitizeProgramProperty(value, this.getKind().getName());
        setReadOnly();
    }

}
