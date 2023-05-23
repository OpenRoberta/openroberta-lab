package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "WIRELESS", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_wireless"})
public final class WirelessActor extends ConfigurationComponent {
    private WirelessActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
