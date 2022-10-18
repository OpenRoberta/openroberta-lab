package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_clear"}, name = "DISPLAY_CLEAR_ACTION")
public final class DisplayClearAction extends ActionWithoutUserChosenName {

    public DisplayClearAction(BlocklyProperties properties, Hide hide) {
        super(properties, hide);
        setReadOnly();
    }

}
