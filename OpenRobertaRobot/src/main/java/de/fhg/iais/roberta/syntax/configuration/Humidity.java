package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "HUMIDITY", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_humidity", "robBrick_humidity"})
public final class Humidity extends ConfigurationComponent {
    private Humidity() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
