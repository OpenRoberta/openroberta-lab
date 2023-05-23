package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DROP", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_drop", "robBrick_drop"})
public final class DropSensor extends ConfigurationComponent {
    private DropSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
