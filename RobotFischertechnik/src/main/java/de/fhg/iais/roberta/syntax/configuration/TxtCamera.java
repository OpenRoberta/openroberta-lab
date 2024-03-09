package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "TXT_CAMERA", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_camera_txt4"})
public final class TxtCamera extends ConfigurationComponent {
    private TxtCamera() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
