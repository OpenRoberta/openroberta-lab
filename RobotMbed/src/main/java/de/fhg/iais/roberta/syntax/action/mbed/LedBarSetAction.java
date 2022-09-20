package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "LEDBAR_SET_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_ledBar_set"})
public final class LedBarSetAction extends Action {

    @NepoValue(name = "X", type = BlocklyType.NUMBER_INT)
    public final Expr x;

    @NepoValue(name = "BRIGHTNESS", type = BlocklyType.NUMBER_INT)
    public final Expr brightness;

    public LedBarSetAction(BlocklyProperties properties, Expr x, Expr brightness) {
        super(properties);
        Assert.notNull(x);
        Assert.notNull(brightness);
        this.x = x;
        this.brightness = brightness;
        setReadOnly();
    }
}
