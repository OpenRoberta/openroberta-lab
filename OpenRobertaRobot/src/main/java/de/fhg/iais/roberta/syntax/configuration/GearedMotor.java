package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "GEARED_MOTOR", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_motor_geared"})
public final class GearedMotor extends ConfigurationComponent {
    private GearedMotor() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
