package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "BUZZER", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_buzzer", "robActions_play_tone"})
public class Buzzer extends ConfigurationComponent {
    private Buzzer() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
