package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "APDS9960", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_apds9960"})
public final class Apds9960Sensor extends ConfigurationComponent {
    private Apds9960Sensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
