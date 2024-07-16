package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "PHOTOTRANSISTOR", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_phototransistor"})
public final class Phototransistor extends ConfigurationComponent {
    private Phototransistor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
