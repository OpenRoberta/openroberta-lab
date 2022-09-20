package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BOB3_RGB_LED_OFF", category = "ACTOR", blocklyNames = {"makeblockActions_leds_off"})
public final class LedOffAction extends Action {

    @NepoField(name = "LEDSIDE")
    public final String side;

    public LedOffAction(BlocklyProperties properties, String side) {
        super(properties);
        this.side = side;
        setReadOnly();
    }
}
