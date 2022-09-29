package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "POTENTIOMETER", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_potentiometer"})
public final class Potentiometer extends ConfigurationComponent {
    private Potentiometer() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
