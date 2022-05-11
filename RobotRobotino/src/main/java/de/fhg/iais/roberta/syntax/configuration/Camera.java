package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "CAMERA", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_camera"})
public final class Camera extends ConfigurationComponent {
    private Camera() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
