package de.fhg.iais.roberta.syntax.configuration.spike;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DISPLAY", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_display"})
public final class Display extends ConfigurationComponent {
    private Display() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
