package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.mode.action.RelayMode;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "RELAY_ACTION", category = "ACTOR", blocklyNames = {"robactions_set_relay"})
public final class RelayAction extends Action {

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoField(name = "RELAYSTATE")
    public final RelayMode mode;

    public RelayAction(BlocklyProperties properties, String port, RelayMode mode) {
        super(properties);
        Assert.isTrue(mode != null);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }
}