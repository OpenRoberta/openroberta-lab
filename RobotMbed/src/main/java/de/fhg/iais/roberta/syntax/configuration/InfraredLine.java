package de.fhg.iais.roberta.syntax.configuration;

import de.fhg.iais.roberta.transformer.forClass.NepoConfiguration;
import de.fhg.iais.roberta.util.dbc.DbcException;

@NepoConfiguration(name = "LINE", category = "CONFIGURATION_SENSOR",
    blocklyNames = {"robConf_line"})
public final class InfraredLine extends ConfigurationComponent {
    private InfraredLine() {
        super(null, null, null, null, null);
        throw new DbcException("should NEVER be called");
    }
}
