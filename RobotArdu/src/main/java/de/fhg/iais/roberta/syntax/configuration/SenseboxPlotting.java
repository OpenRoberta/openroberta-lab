package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SENSEBOX_PLOTTING", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_plotting"})
public final class SenseboxPlotting extends ConfigurationComponent {
    private SenseboxPlotting() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
