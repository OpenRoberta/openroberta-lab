package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "INFRARED", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_infrared", "robBrick_infrared"})
public final class Infrared extends ConfigurationComponent {
    private Infrared() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
