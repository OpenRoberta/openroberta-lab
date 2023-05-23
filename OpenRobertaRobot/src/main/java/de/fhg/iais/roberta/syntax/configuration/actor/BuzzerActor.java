package de.fhg.iais.roberta.syntax.configuration.actor;

import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "BUZZER", category = "CONFIGURATION_ACTOR",
    blocklyNames = {"robConf_buzzer", "robActions_play_tone"})
public final class BuzzerActor extends ConfigurationComponent {
    private BuzzerActor() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
