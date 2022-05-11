package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "OPTICAL", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_optical"})
public final class Optical extends ConfigurationComponent {
    private Optical() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
