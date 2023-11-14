package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "TXT_RGB_GESTURE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_gesture"})
public final class RGBGestureSensor extends ConfigurationComponent {
    private RGBGestureSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
