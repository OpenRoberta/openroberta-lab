package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MOTOR", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_motor", "robConf_motorc"})
public final class MotorActor extends ConfigurationComponent {
    private MotorActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
