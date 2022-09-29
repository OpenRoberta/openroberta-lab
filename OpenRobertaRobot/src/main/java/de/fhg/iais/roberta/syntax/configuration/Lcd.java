package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LCD", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_lcd"})
public final class Lcd extends ConfigurationComponent {
    private Lcd() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
