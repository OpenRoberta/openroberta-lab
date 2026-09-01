package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SCD40", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_scd40"})
public final class Scd40 extends ConfigurationComponent {
    private Scd40() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
