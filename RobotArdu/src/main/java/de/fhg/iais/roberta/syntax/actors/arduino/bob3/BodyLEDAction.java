package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BOB3_BODYLED", category = "ACTOR", blocklyNames = {"bob3Actions_set_led"})
public final class BodyLEDAction extends Action {
    @NepoField(name = "LEDSIDE")
    public final String side;
    @NepoField(name = "LEDSTATE")
    public final String ledState;


    public BodyLEDAction(BlocklyProperties properties, String side, String ledState) {
        super(properties);
        this.ledState = ledState;
        this.side = side;
        setReadOnly();
    }
}
