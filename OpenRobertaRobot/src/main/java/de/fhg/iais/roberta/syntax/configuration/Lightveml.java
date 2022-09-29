package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LIGHTVEML", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_lightveml"})
public final class Lightveml extends ConfigurationComponent {
    private Lightveml() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
