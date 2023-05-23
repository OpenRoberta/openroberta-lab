package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "JOYSTICK", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_joystick"})
public final class JoystickActor extends ConfigurationComponent {
    private JoystickActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
