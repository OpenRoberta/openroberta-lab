package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SENSEBOX_SDCARD", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_sdcard"})
public final class SenseboxSdcard extends ConfigurationComponent {
    private SenseboxSdcard() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
