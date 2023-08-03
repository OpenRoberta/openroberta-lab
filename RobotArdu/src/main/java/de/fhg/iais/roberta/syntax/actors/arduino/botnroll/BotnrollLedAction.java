package de.fhg.iais.roberta.syntax.actors.arduino.botnroll;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BOTNROLL_LED_ACTION", category = "ACTOR", blocklyNames = {"actions_led_botnroll"})
public final class BotnrollLedAction extends Action {

    @NepoField(name = "MODE")
    public final String mode;

    public BotnrollLedAction(BlocklyProperties properties, String mode) {
        super(properties);
        this.mode = mode;
        setReadOnly();
    }
}
