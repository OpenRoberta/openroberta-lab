package de.fhg.iais.roberta.syntax.actors.arduino.bob3;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "BOB3_BODYLED", category = "ACTOR", blocklyNames = {"bob3Actions_set_led"})
public final class BodyLEDAction<V> extends Action<V> {
    @NepoField(name = "LEDSIDE")
    public final String side;
    @NepoField(name = "LEDSTATE")
    public final String ledState;


    public BodyLEDAction(BlocklyBlockProperties properties, BlocklyComment comment, String side, String ledState) {
        super(properties, comment);
        this.ledState = ledState;
        this.side = side;
        setReadOnly();
    }

    public static <V> BodyLEDAction<V> make(String side, String ledState, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BodyLEDAction<>(properties, comment, side, ledState);
    }
    public String getledState() {
        return this.ledState;
    }


    public String getSide() {
        return this.side;
    }

}
