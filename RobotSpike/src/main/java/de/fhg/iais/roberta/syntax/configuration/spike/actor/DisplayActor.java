package de.fhg.iais.roberta.syntax.configuration.spike.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "DISPLAY", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_display"})
public final class DisplayActor extends ConfigurationComponent {
    private DisplayActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
