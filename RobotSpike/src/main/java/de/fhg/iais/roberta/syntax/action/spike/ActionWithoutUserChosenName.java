package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

public abstract class ActionWithoutUserChosenName extends Action implements WithUserDefinedPort {
    @NepoHide
    public final Hide hide;

    public ActionWithoutUserChosenName(BlocklyProperties properties, Hide hide) {
        super(properties);
        this.hide = hide;
    }

    @Override
    public final String getUserDefinedPort() {
        return this.hide.getValue();
    }
}
