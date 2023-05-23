package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LPS22HB", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_lps22hb"})
public final class Lps22hbSensor extends ConfigurationComponent {
    private Lps22hbSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
