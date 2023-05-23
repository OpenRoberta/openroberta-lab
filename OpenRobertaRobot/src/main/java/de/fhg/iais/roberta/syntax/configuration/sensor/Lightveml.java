package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LIGHTVEML", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_lightveml"})
public final class Lightveml extends ConfigurationComponent {
    private Lightveml() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
