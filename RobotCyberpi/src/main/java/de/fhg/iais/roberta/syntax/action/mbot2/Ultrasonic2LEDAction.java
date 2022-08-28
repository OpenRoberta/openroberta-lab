package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_ultrasonic2_led"}, name = "ULTRASONIC2_LIGHT_ACTION")
public final class Ultrasonic2LEDAction extends Action implements WithUserDefinedPort {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoField(name = BlocklyConstants.LED, value = BlocklyConstants.EMPTY_PORT)
    public final String led;
    @NepoValue(name = BlocklyConstants.BRIGHTNESS, type = BlocklyType.NUMBER_INT)
    public final Expr brightness;

    public Ultrasonic2LEDAction(BlocklyProperties properties, String port, String led, Expr brightness) {
        super(properties);
        Assert.notNull(brightness);
        Assert.nonEmptyString(port);
        Assert.nonEmptyString(led);
        this.brightness = brightness;
        this.port = port;
        this.led = led;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }

    public String getLedNumber() {
        return led.replace("LED", "");
    }
}
