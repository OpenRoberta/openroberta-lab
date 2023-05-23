package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "EXTERNAL_LED", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robBrick_led"})
public final class ExternalLedActor extends ConfigurationComponent {
    private ExternalLedActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
