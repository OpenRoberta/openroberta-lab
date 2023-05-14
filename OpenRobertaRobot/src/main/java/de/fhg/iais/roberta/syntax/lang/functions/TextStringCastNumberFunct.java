package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoExpr(name = "TEXT_STRING_CAST_NUMBER_FUNCT", category = "FUNCTION", blocklyNames = {"text_cast_string_tonumber"}, blocklyType = BlocklyType.NUMBER_INT)
public final class TextStringCastNumberFunct extends Function {

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr value;

    public TextStringCastNumberFunct(BlocklyProperties properties, Expr value) {
        super(properties);
        Assert.isTrue(value != null);
        this.value = value;
        setReadOnly();
    }
}
