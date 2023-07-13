package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(name = "LED_ACTION", category = "ACTOR", blocklyNames = {"actions_led", "actions_led_botnroll", "actions_led_edison", "actions_led_nibo"})
public final class LedAction extends Action implements WithUserDefinedPort {
    @NepoField(name = "ACTORPORT")
    public final String port;
    @NepoField(name = "MODE")
    public final String mode;

    public LedAction(BlocklyProperties properties, String port, String mode) {
        super(properties);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}