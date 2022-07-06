package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "DISPLAY_SET_PIXEL", category = "ACTOR", blocklyNames = {"mbedActions_display_setPixel"})
public final class DisplaySetPixelAction<V> extends Action<V> {
    @NepoValue(name = "X", type = BlocklyType.NUMBER)
    public final Expr<V> x;

    @NepoValue(name = "Y", type = BlocklyType.NUMBER)
    public final Expr<V> y;

    @NepoValue(name = "BRIGHTNESS", type = BlocklyType.NUMBER)
    public final Expr<V> brightness;

    public DisplaySetPixelAction(BlocklyProperties properties, Expr<V> x, Expr<V> y, Expr<V> brightness) {
        super(properties);
        Assert.notNull(x);
        Assert.notNull(y);
        Assert.notNull(brightness);
        this.x = x;
        this.y = y;
        this.brightness = brightness;
        setReadOnly();
    }

}
