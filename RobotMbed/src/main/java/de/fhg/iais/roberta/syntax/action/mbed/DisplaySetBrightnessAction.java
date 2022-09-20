package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "DISPLAY_SET_BRIGHTNESS", category = "ACTOR", blocklyNames = {"mbedActions_display_setBrightness"})
public final class DisplaySetBrightnessAction extends Action {

    @NepoValue(name = "BRIGHTNESS", type = BlocklyType.NUMBER_INT)
    public final Expr brightness;

    public DisplaySetBrightnessAction(BlocklyProperties properties, Expr brightness) {
        super(properties);
        Assert.notNull(brightness);
        this.brightness = brightness;
        setReadOnly();
    }
}
