package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_leds_off"}, name = "LEDS_OFF_ACTION")
public final class LedsOffAction extends Action implements WithUserDefinedPort {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoField(name = BlocklyConstants.LED, value = BlocklyConstants.EMPTY_PORT)
    public final String led;
    @NepoHide
    public final Hide hide;

    public LedsOffAction(BlocklyProperties properties, String port, String led, Hide hide) {
        super(properties);
        Assert.nonEmptyString(port);
        Assert.notNull(led);
        this.hide = hide;
        this.port = port;
        this.led = led;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
