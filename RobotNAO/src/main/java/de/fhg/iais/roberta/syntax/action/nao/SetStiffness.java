package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SET_STIFFNESS", category = "ACTOR", blocklyNames = {"naoActions_stiffness"})
public final class SetStiffness extends Action {

    @NepoField(name = "PART")
    public final String bodyPart;

    @NepoField(name = "MODE")
    public final WorkingState onOff;

    public SetStiffness(BlocklyProperties properties, String bodyPart, WorkingState onOff) {
        super(properties);
        Assert.notNull(bodyPart, "Missing body part in SetStiffness block!");
        Assert.notNull(onOff, "Missing onOff in SetStiffness block!");
        this.bodyPart = bodyPart;
        this.onOff = onOff;
        setReadOnly();
    }
}