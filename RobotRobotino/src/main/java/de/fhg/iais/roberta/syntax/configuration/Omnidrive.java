package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "OMNIDRIVE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_omnidrive"})
public final class Omnidrive extends ConfigurationComponent {
    private Omnidrive() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}