package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DIGITAL_INPUT", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_digitalin"})
public final class DigitalInputActor extends ConfigurationComponent {
    private DigitalInputActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
