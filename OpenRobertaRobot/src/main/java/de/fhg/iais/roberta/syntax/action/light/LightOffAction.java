package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "LIGHT_OFF_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_leds_off", "robActions_led_off"})
public final class LightOffAction extends Action implements WithUserDefinedPort {

    @NepoField(name = "ACTORPORT")
    public final String port;

    public LightOffAction(BlocklyProperties properties, String port) {
        super(properties);
        this.port = port;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
