package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

public abstract class ActionWithUserChosenName extends Action implements WithUserDefinedPort {
    @NepoField(name = BlocklyConstants.ACTORPORT)
    public final String port;

    public ActionWithUserChosenName(BlocklyProperties properties, String port) {
        super(properties);
        this.port = port;
    }

    @Override
    public final String getUserDefinedPort() {
        return this.port;
    }
}
