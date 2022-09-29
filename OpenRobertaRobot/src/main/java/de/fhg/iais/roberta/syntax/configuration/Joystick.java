package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "JOYSTICK", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"arduSensors_joystick_getSample", "robConf_joystick"})
public final class Joystick extends ConfigurationComponent {
    private Joystick() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
