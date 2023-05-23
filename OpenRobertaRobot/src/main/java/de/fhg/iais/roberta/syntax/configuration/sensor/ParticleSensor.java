package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "PARTICLE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_particle"})
public final class ParticleSensor extends ConfigurationComponent {
    private ParticleSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
