package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_brickled_off"}, name = "LED_OFF_ACTION")
public final class LedOffAction extends ActionWithoutUserChosenName {

    public LedOffAction(BlocklyProperties properties, Hide hide) {
        super(properties, hide);
        setReadOnly();
    }

}
