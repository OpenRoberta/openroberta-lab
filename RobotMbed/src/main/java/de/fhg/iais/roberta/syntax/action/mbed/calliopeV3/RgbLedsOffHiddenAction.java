package de.fhg.iais.roberta.syntax.action.mbed.calliopeV3;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "RGBLEDS_OFF_HIDDEN_ACTION", category = "ACTOR", blocklyNames = {"actions_rgbLeds_hidden_off_calliopev3"})
public final class RgbLedsOffHiddenAction extends Action {

    @NepoField(name = "SLOT")
    public final String slot;

    @NepoHide
    public final Hide hide;

    public RgbLedsOffHiddenAction(BlocklyProperties properties, String slot, Hide hide) {
        super(properties);
        this.slot = slot;
        this.hide = hide;
        setReadOnly();
    }
}