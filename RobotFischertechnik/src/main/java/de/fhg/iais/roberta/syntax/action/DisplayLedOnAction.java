package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_led_txt"}, name = "DISPLAY_LED_ON_ACTION")
public final class DisplayLedOnAction extends Action {
    @NepoField(name = "MODE")
    public final String mode;
    @NepoField(name = "ACTORPORT")
    public final String port;

    public DisplayLedOnAction(BlocklyProperties properties, String mode, String port) {
        super(properties);
        Assert.notNull(mode);
        Assert.notNull(port);
        this.mode = mode;
        this.port = port;
        setReadOnly();
    }
}
