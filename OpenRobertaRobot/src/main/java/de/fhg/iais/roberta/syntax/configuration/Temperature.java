package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "TEMPERATURE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_temperature", "robBrick_temperature"})
public final class Temperature extends ConfigurationComponent {
    private Temperature() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
