package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoExpr(name = "DISPLAY_GET_PIXEL", category = "ACTOR", blocklyNames = {"mbedActions_display_getPixel"}, blocklyType = BlocklyType.NUMBER_INT)
public final class DisplayGetPixelAction extends Action {

    @NepoValue(name = "X", type = BlocklyType.NUMBER_INT)
    public final Expr x;

    @NepoValue(name = "Y", type = BlocklyType.NUMBER_INT)
    public final Expr y;

    public DisplayGetPixelAction(BlocklyProperties properties, Expr x, Expr y) {
        super(properties);
        Assert.notNull(x);
        Assert.notNull(y);
        this.x = x;
        this.y = y;
        setReadOnly();
    }
}
