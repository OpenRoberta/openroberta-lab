package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "OMNIDRIVE", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_omnidrive"})
public final class OmnidriveActor extends ConfigurationComponent {
    private OmnidriveActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}