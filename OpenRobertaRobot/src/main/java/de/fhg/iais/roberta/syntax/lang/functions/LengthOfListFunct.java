package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoExpr(name = "LENGTH_OF_LIST_FUNCT", category = "FUNCTION", blocklyNames = {"lists_length", "robLists_length"}, blocklyType = BlocklyType.NUMBER)
public final class LengthOfListFunct extends Function {

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr value;

    public LengthOfListFunct(BlocklyProperties properties, Expr value) {
        super(properties);
        Assert.isTrue(value != null);
        this.value = value;
        setReadOnly();
    }
}
