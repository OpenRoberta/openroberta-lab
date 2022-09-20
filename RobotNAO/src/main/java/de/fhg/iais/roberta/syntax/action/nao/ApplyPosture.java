package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "APPLY_POSTURE", category = "ACTOR", blocklyNames = {"naoActions_applyPosture"})
public final class ApplyPosture extends Action {

    @NepoField(name = "DIRECTION")
    public final String posture;

    public ApplyPosture(BlocklyProperties properties, String posture) {
        super(properties);
        Assert.notNull(posture, "Missing posture in ApplyPosture block!");
        this.posture = posture;
        setReadOnly();
    }
}
