package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ODOMETRY", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_odometry"})
public final class OdometrySensor extends ConfigurationComponent {
    private OdometrySensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}