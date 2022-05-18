package de.fhg.iais.roberta.syntax.action.thymio;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robSensors_record_stop"}, name = "RECORD_STOP_ACTION")
public final class RecordStopAction extends Action {

    public RecordStopAction(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
