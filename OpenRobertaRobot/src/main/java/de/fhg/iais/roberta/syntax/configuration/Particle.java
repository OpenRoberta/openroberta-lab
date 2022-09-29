package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "PARTICLE", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_particle"})
public final class Particle extends ConfigurationComponent {
    private Particle() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
