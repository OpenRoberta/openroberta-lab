package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "RGBLED_OFF_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLed_off", "actions_rgbLed_off_mbot", "actions_rgbLed_off_nibo",
    "actions_rgbled_off_nxt", "actions_rgbLed_off_thymio"})
public final class RgbLedOffAction extends Action implements WithUserDefinedPort {

    @NepoField(name = "ACTORPORT")
    public final String port;

    public RgbLedOffAction(BlocklyProperties properties, String port) {
        super(properties);
        this.port = port;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
