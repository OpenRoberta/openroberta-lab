package de.fhg.iais.roberta.syntax.action.mbed.microbitV2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "SOUND_TOGGLE_ACTION", category = "ACTOR", blocklyNames = {"actions_sound_toggle"})
public final class SoundToggleAction extends Action {

    @NepoField(name = "MODE")
    public final String mode;

    @NepoHide
    public final Hide hide;

    public SoundToggleAction(BlocklyProperties properties, String mode, Hide hide) {
        super(properties);
        this.mode = mode;
        this.hide = hide;
        setReadOnly();
    }
}
