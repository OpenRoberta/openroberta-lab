package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "CAMERA_BALLDETECTOR", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_balldetector_txt4"})
public final class Balldetector extends ConfigurationComponent {
    private Balldetector() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
