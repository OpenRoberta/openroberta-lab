package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "ULTRASONIC", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_ultrasonic", "robBrick_ultrasonic"})
public final class Ultrasonic extends ConfigurationComponent {
    private Ultrasonic() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
