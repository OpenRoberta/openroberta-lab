package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "LED_MATRIX__SET_BRIGHTNESS", category = "ACTOR", blocklyNames = {"mBotactions_display_setbrightness"})
public final class LEDMatrixSetBrightnessAction extends Action {

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoValue(name = "BRIGHTNESS", type = BlocklyType.NUMBER_INT)
    public final Expr brightness;

    public LEDMatrixSetBrightnessAction(BlocklyProperties properties, String port, Expr brightness) {
        super(properties);
        Assert.notNull(brightness);
        this.port = port;
        this.brightness = brightness;
        setReadOnly();
    }
}
