package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RGBLED_OFF_HIDDEN_MBOT2_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLed_hidden_off_mbot2"})
public final class Mbot2RgbLedOffHiddenAction extends Action {

    @NepoField(name = "SLOT")
    public final String slot;

    @NepoHide
    public final Hide hide;

    public Mbot2RgbLedOffHiddenAction(BlocklyProperties properties, String slot, Hide hide) {
        super(properties);
        this.slot = slot;
        this.hide = hide;
        setReadOnly();
    }
}
