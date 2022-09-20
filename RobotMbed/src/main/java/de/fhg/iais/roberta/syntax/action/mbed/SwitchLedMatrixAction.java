package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "SWITCH_LED_MATRIX", category = "ACTOR", blocklyNames = {"mbedActions_switch_led_matrix"})
public final class SwitchLedMatrixAction extends Action {

    @NepoField(name = "STATE")
    public final String activated;

    public SwitchLedMatrixAction(BlocklyProperties properties, String activated) {
        super(properties);
        this.activated = activated;
        setReadOnly();
    }
}
