package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "GEARED_MOTOR", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robBrick_motor_geared"})
public final class GearedMotorActor extends ConfigurationComponent {
    private GearedMotorActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
