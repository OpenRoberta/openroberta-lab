package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "CAMERA_COLORDETECTOR", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_colordetector_txt4"})
public final class Colordetector extends ConfigurationComponent {
    private Colordetector() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
