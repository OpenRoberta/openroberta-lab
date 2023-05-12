package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "TEXT_CHAR_CAST_NUMBER_FUNCT", category = "FUNCTION", blocklyNames = {"text_cast_char_tonumber"}, blocklyType = BlocklyType.NUMBER_INT)
public final class TextCharCastNumberFunct extends Function {

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr value;

    @NepoValue(name = "AT", type = BlocklyType.NUMBER_INT)
    public final Expr atIndex;

    public TextCharCastNumberFunct(BlocklyProperties properties, Expr value, Expr atIndex) {
        super(properties);
        this.value = value;
        this.atIndex = atIndex;
        setReadOnly();
    }
}
