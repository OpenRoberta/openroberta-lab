package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "BOTH_MOTORS_STOP_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_motors_stop"})
public final class BothMotorsStopAction extends Action {

    public BothMotorsStopAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
