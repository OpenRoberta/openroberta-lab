package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_led_off_txt"}, name = "DISPLAY_LED_OFF_ACTION")
public final class DisplayLedOffAction extends Action implements WithUserDefinedPort {
    @NepoField(name = "ACTORPORT")
    public final String port;
    @NepoHide
    public final Hide hide;

    public DisplayLedOffAction(BlocklyProperties properties, String port, Hide hide) {
        super(properties);
        Assert.notNull(port);
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return hide.getValue();
    }
}
