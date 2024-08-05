package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "CAMERA_LINE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_line_txt4"})
public final class Linedetector extends ConfigurationComponent {
    private Linedetector() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
