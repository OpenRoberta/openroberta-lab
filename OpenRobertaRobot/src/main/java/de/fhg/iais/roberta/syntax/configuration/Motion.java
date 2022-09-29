package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "MOTION", category = "CONFIGURATION_BLOCK",
    blocklyNames = {"robConf_motion", "robBrick_motion"})
public final class Motion extends ConfigurationComponent {
    private Motion() {
        super(null, true, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
