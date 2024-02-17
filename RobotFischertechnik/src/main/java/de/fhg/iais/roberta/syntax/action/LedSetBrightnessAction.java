package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_led_set_brightness"}, name = "LED_SET_BRIGHTNESS_ACTION")
public final class LedSetBrightnessAction extends Action implements WithUserDefinedPort {
    @NepoValue(name = BlocklyConstants.BRIGHTNESS, type = BlocklyType.NUMBER_INT)
    public final Expr brightness;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public LedSetBrightnessAction(BlocklyProperties properties, Expr brightness, String port) {
        super(properties);
        Assert.notNull(brightness);
        Assert.notNull(port);
        this.brightness = brightness;
        this.port = port;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }

}
