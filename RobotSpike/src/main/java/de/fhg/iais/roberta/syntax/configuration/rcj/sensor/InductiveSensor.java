package de.fhg.iais.roberta.syntax.configuration.rcj.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "INDUCTIVE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_inductive"})
public final class InductiveSensor extends ConfigurationComponent {
    private InductiveSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
