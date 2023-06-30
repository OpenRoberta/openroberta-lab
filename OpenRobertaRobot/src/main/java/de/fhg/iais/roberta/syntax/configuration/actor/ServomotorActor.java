package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SERVOMOTOR", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_servo", "robConf_servoc"})
public final class ServomotorActor extends ConfigurationComponent {
    private ServomotorActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
