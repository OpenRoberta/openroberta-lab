package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(containerType = "TIMER", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robBrick_timer"})
public class Timer extends ConfigurationComponent {
    private Timer() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
