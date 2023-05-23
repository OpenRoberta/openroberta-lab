package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "SENSEBOX_SDCARD", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_sdcard"})
public final class SenseboxSdcardActor extends ConfigurationComponent {
    private SenseboxSdcardActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
