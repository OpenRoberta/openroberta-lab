package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "PULSE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_pulse", "robBrick_pulse"})
public class Pulse extends ConfigurationComponent {
    private Pulse() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
