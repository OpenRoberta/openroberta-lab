package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MBUILD_QUADRGB", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_mbuild_quadrgb"})
public final class MbuildQuadrgbSensor extends ConfigurationComponent {
    private MbuildQuadrgbSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
