package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MEDIUM", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robBrick_motor_middle"})
public final class MediumMotorActor extends ConfigurationComponent {
    private MediumMotorActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
