package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DIFFERENTIALDRIVE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_differentialdrive"})
public class Differentialdrive extends ConfigurationComponent {
    private Differentialdrive() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
