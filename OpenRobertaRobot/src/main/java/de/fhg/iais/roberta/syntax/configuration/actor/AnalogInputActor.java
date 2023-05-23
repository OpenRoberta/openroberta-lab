package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ANALOG_INPUT", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_analogin"})
public final class AnalogInputActor extends ConfigurationComponent {
    private AnalogInputActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
