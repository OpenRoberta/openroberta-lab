package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LOGOTOUCH", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_logotouch"})
public final class Logo extends ConfigurationComponent {
    private Logo() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
