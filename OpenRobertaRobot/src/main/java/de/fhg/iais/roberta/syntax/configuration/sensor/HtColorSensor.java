package de.fhg.iais.roberta.syntax.configuration.sensor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "HT_COLOR", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robBrick_HiTechnic_colour", "robBrick_htcolour"})
public final class HtColorSensor extends ConfigurationComponent {
    private HtColorSensor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
