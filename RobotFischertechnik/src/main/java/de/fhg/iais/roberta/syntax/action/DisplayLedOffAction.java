package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_led_off_txt"}, name = "DISPLAY_LED_OFF_ACTION")
public final class DisplayLedOffAction extends Action {
    @NepoField(name = "ACTORPORT")
    public final String port;

    public DisplayLedOffAction(BlocklyProperties properties, String port) {
        super(properties);
        Assert.notNull(port);
        this.port = port;
        setReadOnly();
    }
}
