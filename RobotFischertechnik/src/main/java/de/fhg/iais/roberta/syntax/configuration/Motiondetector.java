package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "CAMERA_MOTIONDETECTOR", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_motiondetector_txt4"})
public final class Motiondetector extends ConfigurationComponent {
    private Motiondetector() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
