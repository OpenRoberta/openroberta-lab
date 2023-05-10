package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoExpr(name = "IS_LIST_EMPTY_FUNCT", category = "FUNCTION", blocklyNames = {"lists_isEmpty", "robLists_isEmpty"}, blocklyType = BlocklyType.BOOLEAN)
public final class IsListEmptyFunct extends Function {

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr value;

    public IsListEmptyFunct(BlocklyProperties properties, Expr value) {
        super(properties);
        Assert.isTrue(value != null);
        this.value = value;
        setReadOnly();
    }
}
