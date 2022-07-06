package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_led_setBrightness"}, name = "CYBERPI_SET_BRIGHTNESS_ACTION")
public final class LedBrightnessAction extends Action implements WithUserDefinedPort {
    @NepoValue(name = BlocklyConstants.BRIGHTNESS, type = BlocklyType.NUMBER_INT)
    public final Expr brightness;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public LedBrightnessAction(BlocklyProperties properties, Expr brightness, String port, Hide hide) {
        super(properties);
        Assert.notNull(brightness);
        Assert.notNull(port);
        this.brightness = brightness;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }

}
