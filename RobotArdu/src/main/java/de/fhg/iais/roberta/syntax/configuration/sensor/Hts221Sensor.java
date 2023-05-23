package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "HTS221", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_hts221"})
public final class Hts221Sensor extends ConfigurationComponent {
    private Hts221Sensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
