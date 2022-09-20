package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "AUTONOMOUS", category = "ACTOR", blocklyNames = {"naoActions_autonomous"})
public final class Autonomous extends Action {

    @NepoField(name = "MODE")
    public final WorkingState onOff;

    public Autonomous(BlocklyProperties properties, WorkingState onOff) {
        super(properties);
        Assert.notNull(onOff, "Missing onOff in Autonomous block!");
        this.onOff = onOff;
        setReadOnly();
    }
}