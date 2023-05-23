package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SOUND", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_sound", "robConf_sound"})
public final class SoundSensor extends ConfigurationComponent {
    private SoundSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
