package de.fhg.iais.roberta.syntax.action.light;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RGBLED_OFF_HIDDEN_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLed_hidden_off", "actions_rgbLed_hidden_off_calliope"})
public final class RgbLedOffHiddenAction extends Action {
    @NepoHide
    public final Hide hide;

    public RgbLedOffHiddenAction(BlocklyProperties properties, Hide hide) {
        super(properties);
        this.hide = hide;
        setReadOnly();
    }
}
